package org.videohub.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.videohub.constant.VideoHubConstants;
import org.videohub.utils.VideoHubUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 小红书笔记下载实现类
 *
 * @author @fmz200
 * @date 2023-12-18
 */
@Slf4j
public class RedBookService {
    // 下载视频超时时间，默认60秒
    @Value("${videoDownloadTimeout}")
    private static int videoDownloadTimeout;
    // 下载文件的保存位置
    @Value("${fileSavePath}")
    private static String fileSavePath;

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void downloadNodeBatch(String url) {
        try {
            if (videoDownloadTimeout < 1)
                videoDownloadTimeout = 180;
            if (!StringUtils.hasText(fileSavePath))
                fileSavePath = VideoHubConstants.VIDEOHUB_FILEPATH;

            Document doc = Jsoup.connect(url).userAgent(VideoHubConstants.USER_AGENT).get();

            String match = "window.__INITIAL_STATE__";
            for (Element script : doc.select("script")) {
                String scriptData = script.data();
                if (!scriptData.contains(match)) {
                    continue;
                }

                String jsonString = scriptData.substring(scriptData.indexOf("=") + 1);
                log.info("发现笔记信息~~~~");
                JSONObject jsonObject = JSON.parseObject(jsonString);
                JSONObject object = jsonObject.getJSONObject("note");
                String firstNoteId = object.getString("firstNoteId");
                JSONObject note = object.getJSONObject("noteDetailMap").getJSONObject(firstNoteId).getJSONObject("note");

                String ipLocation = Optional.ofNullable(note.getString("ipLocation")).orElse("偶买噶，居然不知道TA在哪");
                JSONObject user = note.getJSONObject("user");
                String userId = user.getString("userId");
                String nickname = user.getString("nickname");
                // String avatar = user.getString("avatar");
                log.info("用户信息-->用户ID：{}，昵称：{}，IP位置：{}", userId, nickname, ipLocation);

                String noteId = note.getString("noteId");
                String title = note.getString("title");
                log.info("笔记信息-->笔记ID：{}，标题：{}", noteId, title);

                // 创建文件夹
                String folder = fileSavePath + "redBook/" + userId + "/" + noteId;
                VideoHubUtils.mkdir4download(folder);
                String projectRoot = System.getProperty("user.dir");
                folder = projectRoot.substring(0, projectRoot.lastIndexOf("/") + 1) + folder;

                // 图片，也可能是视频或者live图的封面
                JSONArray imageList = note.getJSONArray("imageList");
                log.info("共发现{}张图片，开始下载啦！", imageList.size());
                for (Object img : imageList) {
                    JSONObject image = (JSONObject) img;
                    String fileName = matchTraceId(image.getString("urlDefault"));
                    String picUrl = VideoHubConstants.IMAGE_PREFIX + fileName;
                    // String picUrl = image.getString("urlDefault");
                    log.info("download image：{}", picUrl);
                    download(picUrl, fileName, "jpg", folder);
                }

                log.info("[{}]的笔记[{}]图片下载完成啦！\n", nickname, title);

                // 视频
                JSONObject video = note.getJSONObject("video");
                if (video != null) {
                    log.info("发现视频，开始下载啦！");
                    String originVideoKey = video.getJSONObject("consumer").getString("originVideoKey");
                    String videoUrl = VideoHubConstants.VIDEO_PREFIX + originVideoKey;
                    String videoId = video.getJSONObject("media").getString("videoId");
                    log.info("download video：{}", videoUrl);
                    log.warn("视频文件过大可能会下载失败！原因猜测是小红书限流不允许快速下载某个文件！");
                    downloadWithTimeout(videoUrl, videoId, "mp4", folder, videoDownloadTimeout);
                    log.info("[{}]的笔记[{}]视频下载完成啦！\n", nickname, title);
                }

                // TODO live图下载
            }

        } catch (IOException e) {
            log.error("下载文件失败", e);
        }
    }

    public static void downloadUserNodeBatch(String url) {
        try {
            log.info("开始获取用户信息，TA的链接：{}", url);
            Document doc = Jsoup.connect(url).userAgent(VideoHubConstants.USER_AGENT).get();

            String match = "window.__INITIAL_STATE__";
            for (Element script : doc.select("script")) {
                String scriptData = script.data();
                if (!scriptData.contains(match)) {
                    continue;
                }

                log.info("发现用户信息~~~~");
                String jsonString = scriptData.substring(scriptData.indexOf("=") + 1);
                JSONObject jsonObject = JSON.parseObject(jsonString);
                JSONObject object = jsonObject.getJSONObject("user");
                JSONArray nodes = (JSONArray) object.getJSONArray("notes").get(0);
                int count = nodes.size();
                log.info("发现TA的{}条笔记信息，开始下载~~~~", count);
                int index = 0;
                for (Object o : nodes) {
                    JSONObject node = (JSONObject) o;
                    String nodeUrl = VideoHubConstants.REDBOOK_NODE_REGEX_EXPLORE + node.getString("id");
                    log.info("{}/{}，去下载笔记：{}", ++index, count, nodeUrl);
                    downloadNodeBatch(nodeUrl);
                }
            }

        } catch (IOException e) {
            log.error("下载文件失败", e);
        }
    }

    private static void downloadWithTimeout(String url, String fileName, String fileType, String folder, int timeoutInSeconds) {
        CompletableFuture<Void> downloadFuture = CompletableFuture.runAsync(() -> download(url, fileName, fileType, folder), executorService);

        try {
            downloadFuture.get(timeoutInSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("下载超时取消下载，已经超过你设置的 {} 秒", timeoutInSeconds);
            downloadFuture.cancel(true); // 取消下载任务
        } catch (InterruptedException | ExecutionException e) {
            log.error("下载出现错误~~~~", e);
        }
    }

    private static void download(String url, String fileName, String fileType, String folder) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", VideoHubConstants.USER_AGENT);

            log.info("文件将存储到：{}", folder + "/" + fileName + "." + fileType);
            Path filePath = Paths.get(folder, fileName + "." + fileType);
            Files.copy(connection.getInputStream(), filePath);
        } catch (FileAlreadyExistsException e) {
            log.warn("文件已经存在啦！");
        } catch (Exception e) {
            log.error("文件下载错误！", e);
        }
    }

    public static String matchTraceId(String url) {
        // 使用正则表达式匹配目标部分
        String pattern = "(?<=\\/)[^\\/]+(?=!)";
        Pattern targetPattern = Pattern.compile(pattern);
        Matcher matcher = targetPattern.matcher(url);

        // 查找匹配的目标部分
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

}
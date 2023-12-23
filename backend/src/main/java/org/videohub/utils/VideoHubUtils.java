package org.videohub.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public final class VideoHubUtils {
    // 以#开头的行将被忽略，如果你不想下载或某些链接已被下载过，可以在开头添加#
    public static String readLinkFromFile(String fileName) {
        try {
            Path filePath = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
            List<String> lines = Files.readAllLines(filePath);

            // 过滤以"#"开头的行，然后将每行用空格分隔并拼接成一个字符串
            return lines.stream().filter(line -> !line.trim().startsWith("#")).collect(Collectors.joining(" "));
        } catch (IOException | URISyntaxException e) {
            log.error("读取{}文件失败", fileName, e);
            return ""; // 处理文件读取失败的情况
        }
    }

    public static List<String> matchURL(String text, String... regexArray) {
        List<String> matchedValues = new ArrayList<>();

        for (String regex : regexArray) {
            // 使用正则表达式匹配
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            // 查找匹配项
            while (matcher.find()) {
                String match = matcher.group();
                matchedValues.add(match);
                log.info("匹配到URL：{}", match);
            }
        }

        return matchedValues;
    }

    public static void mkdir4download(String folderPath) throws IOException {
        try {
            // 获取项目运行的上级目录
            Path parentDirectory = Paths.get("..").toAbsolutePath().normalize();
            // 创建文件夹路径
            Path fullPath = parentDirectory.resolve(folderPath);
            // 创建文件夹
            Files.createDirectories(fullPath);
            log.info("文件夹创建成功 ---");
        } catch (FileAlreadyExistsException e) {
            log.info("文件夹已经存在 ---");
        } catch (IOException e) {
            log.error("创建文件夹失败", e);
            throw e;
        }
    }
}

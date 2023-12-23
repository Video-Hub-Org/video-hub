package org.videohub.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.videohub.constant.VideoHubConstants;
import org.videohub.controller.RedBookController;
import org.videohub.model.RedBookRequest;
import org.videohub.service.RedBookService;
import org.videohub.utils.VideoHubUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * 本地运行-小红书用户全量笔记下载
 * 如果你是把用户链接直接放到了redbook-user.link文件里，可以直接运行这个类
 *
 * @author @fmz200
 * @date 2023-12-18
 */
@Slf4j
public class RedBookDownloaderUser {

    public static void main(String[] args) {
        // 小红书用户的分享链接
        RedBookRequest redBookRequest = new RedBookRequest();
        redBookRequest.setUrl(VideoHubUtils.readLinkFromFile("redbook-user.link"));
        RedBookController redBookController = new RedBookController();
        redBookController.downloadUserNodeBatch(redBookRequest);
    }

}

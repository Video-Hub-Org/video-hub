package org.videohub.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.videohub.constant.VideoHubConstants;
import org.videohub.controller.RedBookController;
import org.videohub.model.RedBookRequest;
import org.videohub.service.RedBookService;
import org.videohub.utils.VideoHubUtils;

import java.util.List;

/**
 * 本地运行-小红书批量笔记下载
 * 如果你是把笔记链接直接放到了redbook-node.link文件里，可以直接运行这个类
 *
 * @author @fmz200
 * @date 2023-12-18
 */
@Slf4j
public class RedBookDownloaderNode {

    public static void main(String[] args) {
        // 小红书笔记的分享链接
        RedBookRequest redBookRequest = new RedBookRequest();
        redBookRequest.setUrl(VideoHubUtils.readLinkFromFile("redbook-node.link"));
        RedBookController redBookController = new RedBookController();
        redBookController.downloadNodeBatch(redBookRequest);
    }

}

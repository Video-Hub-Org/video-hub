package org.videohub.controller;

import lombok.extern.slf4j.Slf4j;
import org.videohub.model.TikTokRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TikTok&抖音下载
 *
 * @author @fmz200
 * @date 2023-12-18
 */
@Slf4j
@RestController
@RequestMapping("/api/tikiok")
public class TikTokController {

    @PostMapping("/download")
    public ResponseEntity<String> downloadVideo(@RequestBody TikTokRequest videoRequest) {
        log.debug("download video request: {}", videoRequest);
        // 处理视频下载逻辑，返回视频链接
        String videoLink = downloadLogic(videoRequest.getUrl());
        return ResponseEntity.ok(videoLink);
    }

    private String downloadLogic(String videoUrl) {
        // 实现视频下载逻辑，返回视频链接
        // 返回视频链接
        return videoUrl;
    }
}

package org.videohub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.videohub.common.ResponseResult;
import org.videohub.constant.VideoHubConstants;
import org.videohub.model.RedBookRequest;
import org.videohub.service.RedBookService;
import org.videohub.utils.VideoHubUtils;

import java.util.List;

/**
 * 小红书笔记下载
 *
 * @author @fmz200
 * @date 2023-12-18
 */
@Slf4j
@RestController
@RequestMapping("api/redbook")
public class RedBookController {

    /**
     * 批量下载笔记
     *
     * @param redBookRequest 请求URL
     * @return 响应
     */
    @PostMapping("/node/batch")
    public ResponseEntity<ResponseResult<String>> downloadNodeBatch(@RequestBody RedBookRequest redBookRequest) {
        log.debug("download video request: {}", redBookRequest);
        List<String> urls = VideoHubUtils.matchURL(redBookRequest.getUrl(), VideoHubConstants.REDBOOK_NODE_REGEX_01,
                VideoHubConstants.REDBOOK_NODE_REGEX_02, VideoHubConstants.REDBOOK_NODE_REGEX_03);
        if (CollectionUtils.isEmpty(urls)) {
            return ResponseEntity.ok(new ResponseResult<>(0, "没有获取到任何链接，检查一下吧~~~~"));
        }
        log.info("共发现条{}链接，开始下载喽~~~~", urls.size());
        for (String url : urls) {
            // 处理视频下载逻辑，返回视频链接
            RedBookService.downloadNodeBatch(url);
        }
        log.info("所有的笔记都下载完成啦!");
        return ResponseEntity.ok(new ResponseResult<>(1, "共发现条" + urls.size() + "链接，下载完成喽~~~~"));
    }

    /**
     * 批量下载多用户的所有笔记
     *
     * @param redBookRequest 请求URL
     * @return 响应
     */
    @PostMapping("/user/batch")
    public ResponseEntity<ResponseResult<String>> downloadUserNodeBatch(@RequestBody RedBookRequest redBookRequest) {
        log.debug("download request: {}", redBookRequest);
        List<String> urls = VideoHubUtils.matchURL(redBookRequest.getUrl(), VideoHubConstants.REDBOOK_USER_REGEX_01);
        if (CollectionUtils.isEmpty(urls)) {
            return ResponseEntity.ok(new ResponseResult<>(0, "没有获取到任何链接，检查一下吧~~~~"));
        }
        log.info("共发现条{}用户链接，开始下载喽~~~~", urls.size());
        for (String url : urls) {
            // 处理视频下载逻辑，返回视频链接
            RedBookService.downloadUserNodeBatch(url);
        }
        log.info("所有的笔记都下载完成啦!");
        return ResponseEntity.ok(new ResponseResult<>(1, "共发现条" + urls.size() + "链接，下载完成喽~~~~"));
    }

}

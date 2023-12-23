package org.videohub.constant;

/**
 * 常量
 */
public class VideoHubConstants {
    // 文件下载路径，默认为项目同级目录下
    public static final String VIDEOHUB_FILEPATH = "videoHubDownload/";

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    // 小红书笔记匹配正则
    public static final String REDBOOK_NODE_REGEX_01 = "http://xhslink\\.com/\\w+";
    public static final String REDBOOK_NODE_REGEX_02 = "https://www.xiaohongshu.com/explore/([a-zA-Z0-9]+)";
    public static final String REDBOOK_NODE_REGEX_03 = "https://www.xiaohongshu.com/discovery/item/([a-zA-Z0-9]+)";

    // 小红书用户匹配正则
    public static final String REDBOOK_USER_REGEX_01 = "https://www.xiaohongshu.com/user/profile/([a-zA-Z0-9]+)";

    public static final String REDBOOK_NODE_REGEX_EXPLORE = "https://www.xiaohongshu.com/explore/";

    public static final String IMAGE_PREFIX = "https://sns-img-qc.xhscdn.com/";
    public static final String VIDEO_PREFIX = "http://sns-video-qc.xhscdn.com/";

}

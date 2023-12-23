package org.videohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * VideoHub主程序入口
 *
 * @author fmz200
 * @date 2023-12-18
 */

@SpringBootApplication
public class VideoHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoHubApplication.class, args);
    }

}
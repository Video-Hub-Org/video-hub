package org.videohub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*") // 允许的前端域
                // 允许多个特定域名访问：
                // .allowedOriginPatterns("http://yourdomain1.com", "http://yourdomain2.com")
                .allowedMethods("POST");
    }
}

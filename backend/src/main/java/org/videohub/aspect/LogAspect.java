package org.videohub.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Aspect
@Component
public class LogAspect {
    // 开始时间
    private Instant startInstant;

    @Before("execution(* org.videohub.controller..*.*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        startInstant = Instant.now();
        log.info("Method {} is about to be executed.", joinPoint.getSignature().toShortString());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String clientIP = getClientIP(request);
        log.info("Client IP: {}", clientIP);

        // 如果需要获取方法参数，可以使用以下方式
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("Method argument: {}", arg);
        }

        // 记录方法开始时间
        log.info("Method start time: {}", formatInstant(startInstant));
    }

    @After("execution(* org.videohub.controller..*.*(..))")
    public void logAfterMethodExecution(JoinPoint joinPoint) {
        // 结束时间
        Instant endInstant = Instant.now();
        log.info("Method {} has been executed.", joinPoint.getSignature().toShortString());

        // 记录方法结束时间
        log.info("Method end time: {}", formatInstant(endInstant));

        // 计算耗时
        Duration duration = Duration.between(startInstant, endInstant);
        long elapsedTimeSeconds = duration.toSeconds();
        long elapsedTimeMinutes = duration.toMinutes();

        // 打印开始和结束时间以及耗时
        log.info("下载开始时间: " + formatInstant(startInstant));
        log.info("下载结束时间: " + formatInstant(endInstant));
        log.info("下载总共耗时: " + elapsedTimeSeconds + " 秒");
        log.info("下载总共耗时: " + elapsedTimeMinutes + " 分钟");
    }

    private String formatInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));
        return formatter.format(instant);
    }

    // 获取客户端IP地址
    private String getClientIP(HttpServletRequest request) {
        String clientIP = request.getHeader("X-Forwarded-For");

        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("Proxy-Client-IP");
        }
        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            clientIP = request.getRemoteAddr();
        }

        return clientIP;
    }
}

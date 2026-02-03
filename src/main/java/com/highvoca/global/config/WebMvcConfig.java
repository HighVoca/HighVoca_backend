package com.highvoca.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://13.125.xxx.xxx:3000",
                        "https://highvoca.cloud",
                        "https://www.highvoca.cloud",
                        "http://3.36.205.51:8080"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // 쿠키/인증 정보 포함 허용
                .maxAge(3600);
    }
}
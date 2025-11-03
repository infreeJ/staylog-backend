package com.staylog.staylog.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.image-location}")
    private String imageLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 경로로 들어오는 요청을 imageLocation에 매핑
        // file: 접두사를 사용하여 로컬 파일 시스템 경로를 지정
        // 마지막에 /를 붙여 디렉토리임을 명시
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + imageLocation + "/");
    }
}
package com.staylog.staylog.global.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 설정
 * - Mapper 인터페이스 스캔
 * - application-common.yml에서 mapper-locations, type-aliases-package 설정
 */
@Configuration
@MapperScan(basePackages = {
        "com.staylog.staylog.domain.*.mapper",           // 도메인별 Mapper
        "com.staylog.staylog.domain.admin.*.mapper",     // Admin 하위 도메인 Mapper
        "com.staylog.staylog.global.security.mapper"     // Security Mapper (RefreshToken)
})
public class MyBatisConfig {
}

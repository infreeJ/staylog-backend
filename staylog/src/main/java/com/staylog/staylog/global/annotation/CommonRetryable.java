package com.staylog.staylog.global.annotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.staylog.staylog.global.exception.BusinessException;
import com.staylog.staylog.global.exception.custom.InfrastructureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;


/**
 * 예외 발생 시 재시도 어노테이션
 * @author 이준혁
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Retryable(
        retryFor = { // 재시도 대상 예외
                TransientDataAccessException.class // Spring의 일시적 DB 오류
        },
        noRetryFor = { // 재시도 제외 예외
                BusinessException.class, // 재시도 불가능한 커스텀 예외 클래스
                DataIntegrityViolationException.class,
                NullPointerException.class,
                IllegalArgumentException.class,
                JsonProcessingException.class
        },
        maxAttempts = 3, // 최대 재시도 횟수
        backoff = @Backoff(delay = 1000, multiplier = 2) // 1, 2 ,4초 간격으로 재시도
)
public @interface CommonRetryable {
}

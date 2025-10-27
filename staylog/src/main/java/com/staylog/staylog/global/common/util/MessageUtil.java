package com.staylog.staylog.global.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 메시지 유틸리티
 * MessageSource를 사용하여 메시지 키로 실제 메시지를 가져옴
 *
 * @author 이준혁
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    /**
     * 메시지 키로 메시지 가져오기 (현재 로케일)
     *
     * @param key 메시지 키 (예: "success.mail.sent")
     * @return 메시지 텍스트
     */
    public String getMessage(String key) {
        return getMessage(key, null, LocaleContextHolder.getLocale());
    }

    /**
     * 메시지 키로 메시지 가져오기 (파라미터 포함, 현재 로케일)
     *
     * @param key 메시지 키
     * @param args 메시지 파라미터 배열 (예: new Object[]{"사용자명"})
     * @return 메시지 텍스트
     */
    public String getMessage(String key, Object[] args) {
        return getMessage(key, args, LocaleContextHolder.getLocale());
    }

    /**
     * 메시지 키로 메시지 가져오기 (특정 로케일)
     *
     * @param key 메시지 키
     * @param locale 로케일
     * @return 메시지 텍스트
     */
    public String getMessage(String key, Locale locale) {
        return getMessage(key, null, locale);
    }

    /**
     * 메시지 키로 메시지 가져오기 (파라미터 포함, 특정 로케일)
     *
     * @param key 메시지 키
     * @param args 메시지 파라미터 배열
     * @param locale 로케일
     * @return 메시지 텍스트
     */
    public String getMessage(String key, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            log.warn("메시지 키를 찾을 수 없습니다: {}", key);
            return key; // 메시지 키를 찾을 수 없으면 키 자체를 반환
        }
    }

    /**
     * 기본 메시지와 함께 메시지 가져오기
     * 메시지 키를 찾을 수 없으면 기본 메시지 반환
     *
     * @param key 메시지 키
     * @param defaultMessage 기본 메시지
     * @return 메시지 텍스트
     */
    public String getMessageOrDefault(String key, String defaultMessage) {
        try {
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            log.warn("메시지 키를 찾을 수 없습니다: {}, 기본 메시지 사용: {}", key, defaultMessage);
            return defaultMessage;
        }
    }

    /**
     * 한국어 메시지 가져오기
     *
     * @param key 메시지 키
     * @return 한국어 메시지
     */
    public String getKoreanMessage(String key) {
        return getMessage(key, null, Locale.KOREAN);
    }

    /**
     * 영어 메시지 가져오기
     *
     * @param key 메시지 키
     * @return 영어 메시지
     */
    public String getEnglishMessage(String key) {
        return getMessage(key, null, Locale.ENGLISH);
    }
}

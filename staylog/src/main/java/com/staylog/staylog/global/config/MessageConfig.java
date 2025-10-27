package com.staylog.staylog.global.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

/**
 * messages.properties를 통해서 메시지 관리힉;
 *
 * @author 이준혁
 */
@Configuration
public class MessageConfig {

    /**
     * MessageSource Bean 설정
     * - messages.properties 파일을 읽어서 메시지를 관리
     * - UTF-8 인코딩 설정
     * - 캐시 시간 설정 (개발: 3초, 운영: -1로 무제한)
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        // messages.properties 파일 경로 설정
        messageSource.setBasenames(
            "classpath:messages",
            "classpath:validation-messages"
        );

        // 기본 인코딩 UTF-8
        messageSource.setDefaultEncoding("UTF-8");

        // 기본 로케일 설정 (한국어)
        messageSource.setDefaultLocale(Locale.KOREAN);

        // 캐시 시간 설정 (초 단위)
        // -1: 캐시 무제한 (운영 환경)
        // 3: 3초마다 리로드 (개발 환경)
        messageSource.setCacheSeconds(3);

        // 메시지 코드를 찾을 수 없을 때 에러 대신 코드 반환
        messageSource.setUseCodeAsDefaultMessage(true);

        return messageSource;
    }

    /**
     * Validator에 MessageSource 적용
     * - @Valid 어노테이션 검증 시 메시지 소스 사용
     */
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}

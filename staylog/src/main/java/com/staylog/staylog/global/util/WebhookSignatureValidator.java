package com.staylog.staylog.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 웹훅 시그니처 검증 유틸리티
 * - Toss Payments 웹훅 요청의 무결성 검증
 * - HMAC-SHA256 기반 서명 검증
 */
@Component
@Slf4j
public class WebhookSignatureValidator {

    private static final String HMAC_SHA256 = "HmacSHA256";

    /**
     * 웹훅 시그니처 검증
     *
     * @param payload 요청 본문 (raw JSON string)
     * @param signature 요청 헤더의 서명 값 (Toss-Signature)
     * @param secret 웹훅 시크릿 키 (application.yml)
     * @return 검증 성공 여부
     */
    public boolean validateSignature(String payload, String signature, String secret) {
        try {
            String calculatedSignature = calculateSignature(payload, secret);
            boolean isValid = calculatedSignature.equals(signature);

            if (!isValid) {
                log.warn("웹훅 시그니처 불일치: expected={}, actual={}", calculatedSignature, signature);
            }

            return isValid;
        } catch (Exception e) {
            log.error("웹훅 시그니처 검증 실패: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * HMAC-SHA256 서명 계산
     *
     * @param payload 요청 본문
     * @param secret 웹훅 시크릿 키
     * @return Base64 인코딩된 서명 값
     */
    private String calculateSignature(String payload, String secret)
            throws NoSuchAlgorithmException, InvalidKeyException {

        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8),
            HMAC_SHA256
        );
        mac.init(secretKeySpec);

        byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}

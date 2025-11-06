package com.staylog.staylog.external.toss.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.staylog.staylog.external.toss.dto.response.TossPaymentResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Toss 웹훅 요청 DTO
 * - 계좌이체 입금 확인 시 Toss에서 전송하는 웹훅 페이로드
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TossWebhookRequest {

    /**
     * 이벤트 타입
     * - PAYMENT_STATUS_CHANGED: 결제 상태 변경 (계좌이체 입금 확인 등)
     */
    private String eventType;

    /**
     * 이벤트 발생 시각
     */
    private LocalDateTime createdAt;

    /**
     * 결제 정보
     */
    private TossPaymentResponse data;
}

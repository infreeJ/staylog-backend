package com.staylog.staylog.external.toss.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.staylog.staylog.external.toss.dto.response.TossVirtualAccountResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Toss v2 가상계좌 웹훅 요청 DTO
 * - v2 가상계좌 입금 확인 시 Toss에서 전송하는 웹훅 페이로드
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TossVirtualAccountWebhookRequest {

    /**
     * 이벤트 타입
     * - VirtualAccount.Deposit: 가상계좌 입금 완료
     */
    private String eventType;

    /**
     * 이벤트 발생 시각
     */
    private LocalDateTime createdAt;

    /**
     * 가상계좌 정보
     */
    private TossVirtualAccountResponse data;
}

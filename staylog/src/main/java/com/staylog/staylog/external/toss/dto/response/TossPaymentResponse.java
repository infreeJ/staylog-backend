package com.staylog.staylog.external.toss.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TossPaymentResponse {
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String method;          // 결제 수단 (카드, 계좌이체 등)
    private Long totalAmount;       // 총 결제 금액
    private Long balanceAmount;     // 취소 가능 잔액
    private String status;          // READY, IN_PROGRESS, DONE, CANCELED
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private String lastTransactionKey;
    private List<Cancel> cancels;   // 취소 내역
    private Card card;              // 카드 결제 정보
    private EasyPay easyPay;        // 간편결제 정보
    private Failure failure;        // 실패 정보
}

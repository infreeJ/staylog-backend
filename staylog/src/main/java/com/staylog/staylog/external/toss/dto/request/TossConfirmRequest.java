package com.staylog.staylog.external.toss.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossConfirmRequest {
    private String paymentKey;   // 토스가 발급한 결제 키
    private String orderId;      // 우리가 생성한 주문 번호 (BOOKING_NUM)
    private Long amount;         // 결제 금액
}

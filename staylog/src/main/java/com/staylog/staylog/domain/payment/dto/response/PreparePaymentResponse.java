package com.staylog.staylog.domain.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 준비 응답 DTO
 * 프론트엔드에서 Toss SDK 초기화에 필요한 정보 반환
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreparePaymentResponse {

    private Long paymentId;
    private String orderId;        // BOOKING_NUM (Toss에 전달할 주문번호)
    private Long amount;
    private String method;
    private String clientKey;      // Toss 클라이언트 키 (프론트엔드에서 SDK 초기화용)
    private String customerName;   // 고객 이름
}

package com.staylog.staylog.domain.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 결제 Entity
 * PAYMENT 테이블과 1:1 매핑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    // 기본 정보
    private Long paymentId;
    private String status;          // PaymentStatus code (PAY_READY, PAY_PAID, PAY_FAILED, etc.)
    private Long amount;            // 최종 결제 금액 (할인 적용 후)
    private String method;          // 결제 수단 (CARD, TRANSFER, etc.)
    private Long bookingId;         // 예약 ID (FK)
    private String paymentKey;      // Toss 결제 키
    
    // 쿠폰 할인 정보
    private Long couponId;          // 사용한 쿠폰 ID (nullable)
    private Long originalAmount;    // 원래 금액 (할인 전)
    private Long discountAmount;    // 할인 금액

    // 타임스탬프
    private OffsetDateTime requestedAt;  // 결제 요청 시각
    private OffsetDateTime approvedAt;   // 결제 승인 시각
    private OffsetDateTime refundedAt;   // 환불 시각

    // 환불 정보
    private Long refundId;          // 환불 ID (nullable)
    private Long refundAmount;      // 환불 금액
    
    //가상계좌 웹훅에 필요한 필드
    private String virtualAccountBank;
    private String virtualAccountNumber;
    private String virtualAccountHolder;
    private OffsetDateTime virtualAccountDueDate;
}

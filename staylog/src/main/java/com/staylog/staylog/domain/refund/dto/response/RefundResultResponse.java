package com.staylog.staylog.domain.refund.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 환불 처리 결과 응답 DTO
 * - Toss API 호출 후 최종 결과
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResultResponse {

    private Long refundId;               // 환불 ID
    private Long paymentId;              // 결제 ID
    private String paymentKey;           // Toss 결제 키

    private Long refundAmount;           // 환불 금액
    private String refundType;           // 환불 유형
    private String refundReason;         // 환불 사유

    private String refundStatus;         // 환불 상태
    private String paymentStatus;        // 결제 상태
    private String bookingStatus;        // 예약 상태

    private LocalDateTime requestedAt;   // 환불 요청 시각
    private LocalDateTime completedAt;   // 환불 완료 시각

    private String failureReason;        // 실패 사유 (실패 시)
}

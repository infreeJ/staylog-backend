package com.staylog.staylog.domain.refund.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 환불 상세 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundDetailResponse {

    private Long refundId;               // 환불 ID
    private Long paymentId;              // 결제 ID
    private Long bookingId;              // 예약 ID
    private String bookingNum;           // 예약 번호

    private Long totalAmount;            // 원 결제 금액
    private Long refundAmount;           // 환불 금액
    private String refundType;           // 환불 유형 (FULL, EIGHTY, FIFTY, THIRTY, CUSTOM)
    private String refundReason;         // 환불 사유

    private String status;               // 환불 상태 (REQUESTED, PENDING, APPROVED, COMPLETED, REJECTED, FAILED)
    private String paymentStatus;        // 결제 상태
    private String bookingStatus;        // 예약 상태

    private LocalDate checkInDate;       // 체크인 날짜
    private LocalDateTime requestedAt;   // 환불 요청 시각
    private LocalDateTime approvedAt;    // 환불 승인 시각
    private LocalDateTime completedAt;   // 환불 완료 시각

    private String failureReason;        // 실패 사유 (실패 시)
    private String policyMessage;        // 환불 정책 안내 메시지
}

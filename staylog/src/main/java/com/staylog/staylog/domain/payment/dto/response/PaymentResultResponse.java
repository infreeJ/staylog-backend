package com.staylog.staylog.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 결제 결과 응답 DTO
 * 결제 승인 후 결제 및 예약 상태 반환
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResultResponse {

    private Long paymentId;
    private String paymentKey;

    @JsonProperty("bookingId")
    private Long bookingId;
    private String orderId;
    private Long amount;
    private String method;

    // 상태 정보
    private String paymentStatus;      // PaymentStatus code (PAY_PAID, PAY_FAILED, etc.)
    private String reservationStatus;  // ReservationStatus code (RES_CONFIRMED, RES_CANCELED, etc.)

    // 타임스탬프
    private OffsetDateTime requestedAt;
    //2025-11-08T00:41:27+09:00 이런식으로 들어와서 OffsetDateTime 써야함
    private OffsetDateTime approvedAt;

    // 실패 시 정보 (optional)
    private String failureReason;
}

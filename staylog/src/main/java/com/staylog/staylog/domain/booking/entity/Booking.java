package com.staylog.staylog.domain.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 예약 Entity
 * RESERVATION 테이블과 1:1 매핑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    // 기본 정보
    private Long bookingId;
    private Long userId;
    private Long roomId;
    private String bookingNum;      // 주문번호 (orderId for Toss)
    private Long amount;            // 할인 전 원래 금액
    private Long finalAmount;       // 할인 후 최종 금액 (쿠폰 적용)

    // 예약 정보
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;          // ReservationStatus code (RES_PENDING, RES_CONFIRMED, etc.)
    private String guestName;

    // 인원 정보
    private Integer adults;
    private Integer children;
    private Integer infants;
    private Integer totalGuestCount;

    // 타임스탬프
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;  // 만료 시각

    // 결제 정보
    private Long paymentId;          // PAY_ID (nullable)

    // 리뷰 작성 여부
    private String isWrited;         // 'Y' or 'N'
}

package com.staylog.staylog.global.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 승인 이벤트 DTO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmEvent {
    private long paymentId; // 결제 PK
    private long bookingId; // 예약 PK
    private long TotalAmount; // 총 결제 금액
    private long couponId;
}

package com.staylog.staylog.global.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefundConfirmEvent {
    private long refundId;
    private long paymentId;
    private long bookingId;
    private long refundAmount;
}

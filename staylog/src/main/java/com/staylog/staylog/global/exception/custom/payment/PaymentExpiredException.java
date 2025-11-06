package com.staylog.staylog.global.exception.custom.payment;

import com.staylog.staylog.global.common.code.ErrorCode;

/**
 * 결제 만료 예외
 */
public class PaymentExpiredException extends PaymentException {
    public PaymentExpiredException() {
        super(ErrorCode.PAYMENT_EXPIRED);
    }

    public PaymentExpiredException(Long paymentId) {
        super(ErrorCode.PAYMENT_EXPIRED,
              String.format("Payment expired: paymentId=%d", paymentId));
    }
}

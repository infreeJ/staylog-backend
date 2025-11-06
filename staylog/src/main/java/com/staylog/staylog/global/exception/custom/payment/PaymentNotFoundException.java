package com.staylog.staylog.global.exception.custom.payment;

import com.staylog.staylog.global.common.code.ErrorCode;

/**
 * 결제를 찾을 수 없을 때 발생하는 예외
 * HTTP 404 Not Found
 */
public class PaymentNotFoundException extends PaymentException {

    public PaymentNotFoundException() {
        super(ErrorCode.PAYMENT_NOT_FOUND);
    }

    public PaymentNotFoundException(Long paymentId) {
        super(ErrorCode.PAYMENT_NOT_FOUND,
              String.format("Payment not found: paymentId=%d", paymentId));
    }

    public PaymentNotFoundException(String paymentKey) {
        super(ErrorCode.PAYMENT_NOT_FOUND,
              String.format("Payment not found: paymentKey=%s", paymentKey));
    }

    public PaymentNotFoundException(String field, Object value) {
        super(ErrorCode.PAYMENT_NOT_FOUND,
              String.format("Payment not found: %s=%s", field, value));
    }
}

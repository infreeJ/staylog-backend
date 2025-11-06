package com.staylog.staylog.global.exception.custom.payment;

import com.staylog.staylog.global.common.code.ErrorCode;

/**
 * 결제 실패 예외
 */
public class PaymentFailedException extends PaymentException {
    public PaymentFailedException(String message) {
        super(ErrorCode.PAYMENT_FAILED, message);
    }
}

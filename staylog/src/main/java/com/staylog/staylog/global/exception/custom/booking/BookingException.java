package com.staylog.staylog.global.exception.custom.booking;

import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;

/**
 * 예약 관련 기본 예외 클래스
 */
public class BookingException extends BusinessException {
    public BookingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BookingException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}

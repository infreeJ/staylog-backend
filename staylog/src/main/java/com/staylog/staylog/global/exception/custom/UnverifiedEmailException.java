package com.staylog.staylog.global.exception.custom;

import com.staylog.staylog.global.common.code.ErrorCode;

public class UnverifiedEmailException extends RuntimeException {
    public UnverifiedEmailException(ErrorCode errorCode, String message) {
        super(message);
    }
}

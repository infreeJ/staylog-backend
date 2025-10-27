package com.staylog.staylog.global.exception.custom;

import com.staylog.staylog.global.common.code.ErrorCode;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(ErrorCode errorCode, String message) {
        super(message);
    }
}
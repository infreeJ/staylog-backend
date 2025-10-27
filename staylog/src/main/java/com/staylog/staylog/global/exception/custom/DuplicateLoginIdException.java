package com.staylog.staylog.global.exception.custom;

import com.staylog.staylog.global.common.code.ErrorCode;

public class DuplicateLoginIdException extends RuntimeException {
    public DuplicateLoginIdException(ErrorCode errorCode, String message) {
        super(message);
    }
}

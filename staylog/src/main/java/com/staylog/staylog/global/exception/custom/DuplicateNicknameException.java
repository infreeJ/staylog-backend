package com.staylog.staylog.global.exception.custom;

import com.staylog.staylog.global.common.code.ErrorCode;

public class DuplicateNicknameException extends RuntimeException {
    public DuplicateNicknameException(ErrorCode errorCode, String message) {
        super(message);
    }
}

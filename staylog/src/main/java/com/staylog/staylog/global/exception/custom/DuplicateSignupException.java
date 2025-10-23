package com.staylog.staylog.global.exception.custom;

public class DuplicateSignupException extends RuntimeException {

    /**
     * 회원가입 시 아이디 중복 예외 처리 객체
     * @auther 이준혁
     */
    public DuplicateSignupException(String message) {
        super(message);
    }
}

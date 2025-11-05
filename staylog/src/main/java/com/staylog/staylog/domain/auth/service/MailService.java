package com.staylog.staylog.domain.auth.service;

import java.time.LocalDateTime;

public interface MailService {

    /**
     * 인증 메일을 발송하고, 인증 정보를 DB에 저장
     * @param email 인증을 받을 이메일 주소
     * @return 인증 코드 만료 시간
     */
    LocalDateTime sendVerificationMail(String email);

    /**
     * 이메일과 인증번호를 받아 유효성을 검증
     * @param email 이메일 주소
     * @param code 사용자가 입력한 인증번호
     * @return 인증 성공 여부
     */
    boolean verifyMail(String email, String code);


    /**
     * 만료된 이메일 데이터 삭제
     * @author 이준혁
     */
    public void deleteExpiredEmail();
}

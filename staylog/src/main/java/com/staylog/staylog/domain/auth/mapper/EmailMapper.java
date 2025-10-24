package com.staylog.staylog.domain.auth.mapper;

import com.staylog.staylog.domain.auth.dto.EmailVerificationDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {

    /**
     * 이메일 인증 여부 검증
     * @author 이준혁
     * @param email
     * @return 이메일 데이터
     */
    public EmailVerificationDto findVerificationByEmail(String email);

    /**
     * 이메일 인증 데이터 저장 또는 업데이트
     * @author 이준혁
     * @param emailVerificationDto
     * @return 성공 여부 1 또는 0
     */
    public int saveOrUpdateVerification(EmailVerificationDto emailVerificationDto);

    /**
     * 이메일 데이터 삭제
     * @author 이준혁
     * @param email
     * @return 성공 여부 1 또는 0
     */
    public int deleteVerificationByEmail(String email);
}

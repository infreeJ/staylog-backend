package com.staylog.staylog.domain.auth.repository;

import com.staylog.staylog.domain.auth.dto.EmailVerificationDto;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface AuthMapper {
    public int createUser(SignupRequest signupRequest);


    public EmailVerificationDto findVerificationByEmail(String email);

    public int saveOrUpdateVerification(EmailVerificationDto emailVerificationDto);

    public int deleteVerificationByEmail(String email);

    /**
     * 마지막 로그인 시간 업데이트
     * @param userId 사용자 ID
     * @param lastLogin 마지막 로그인 시간
     * @return 업데이트된 행 수
     */
    int updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);
}

package com.staylog.staylog.domain.auth.mapper;

import com.staylog.staylog.domain.auth.dto.EmailVerificationDto;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface AuthMapper {

    /**
     * 유저 회원가입
     * @author 이준혁
     * @param signupRequest
     * @return 성공 여부 1 또는 0
     */
    public int createUser(SignupRequest signupRequest);


    /**
     * 마지막 로그인 시간 업데이트
     * @param userId 사용자 ID
     * @param lastLogin 마지막 로그인 시간
     * @return 업데이트된 행 수
     */
    int updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);
}

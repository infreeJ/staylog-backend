package com.staylog.staylog.global.security.mapper;

import com.staylog.staylog.global.security.entity.RefreshToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {

    // RefreshToken DB에 저장
    void save(RefreshToken refreshToken);

    // 토큰 문자열로 RefreshToken 조회
    Optional<RefreshToken> findByToken(@Param("token") String token);

    // RefreshToken 업데이트 (재발급 시 사용)
    void updateToken(RefreshToken refreshToken);

    // 토큰 문자열로 RefreshToken 삭제
    void deleteByToken(@Param("token") String token);

    // 사용자 ID로 RefreshToken 삭제 (로그인 중복 안되게 로그인시 기존 RefreshToken 삭제)
    void deleteByUserId(@Param("userId") Long userId);

    // 만료된 RefreshToken들 일괄 삭제
    void deleteExpiredTokens();
}

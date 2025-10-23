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

    // 사용자 ID로 RefreshToken 조회 (최신 1개)
    Optional<RefreshToken> findByUserId(@Param("userId") Long userId);

    // RefreshToken 업데이트 (재발급 시 사용)
    void updateToken(RefreshToken refreshToken);

    // 토큰 문자열로 RefreshToken 삭제
    void deleteByToken(@Param("token") String token);

    // 사용자 ID로 RefreshToken 삭제 (로그아웃 시 사용)
    void deleteByUserId(@Param("userId") Long userId);

    // 만료된 RefreshToken들 일괄 삭제
    void deleteExpiredTokens();
}

package com.staylog.staylog.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 토큰 갱신 응답 DTO
 * AccessToken 갱신 시 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    /**
     * 새로 발급된 JWT Access Token
     */
    private String accessToken;

    /**
     * 토큰 타입 (항상 "Bearer")
     */
    private String tokenType;

    /**
     * Access Token 만료 시간 (밀리초)
     */
    private Long expiresIn;
}

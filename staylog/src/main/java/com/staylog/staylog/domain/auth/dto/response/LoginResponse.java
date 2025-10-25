package com.staylog.staylog.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 로그인 응답 DTO
 * AccessToken은 응답 본문에, RefreshToken은 HttpOnly 쿠키로 전달
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    // Access Token (클라이언트쪽에서 Authorization 헤더로 사용)
    private String accessToken;
    //항상 Bearer
    private String tokenType;
    //만료시간
    private Long expiresIn;
    //유저 기본 정보
    private UserInfo user;

    /**
     * 중첩 클래스: 로그인 응답에 포함할 사용자 정보
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {

        private Long userId;
        private String loginId;
        private String nickname;
        private String email;
        private String profileImage;
        private String role;
        private String status;
        private LocalDateTime lastLogin;
    }
}

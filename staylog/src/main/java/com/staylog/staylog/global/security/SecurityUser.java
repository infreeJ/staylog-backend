package com.staylog.staylog.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JWT 인증된 사용자 정보를 담는 클래스
 * SecurityContext의 principal로 사용됨
 */
@Getter
@AllArgsConstructor
public class SecurityUser {

    private final Long userId;
    private final String email;
    private final String role;
    private final String nickname;
    private final String loginId;
}

package com.staylog.staylog.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원가입 응답 DTO
 * @author 임채호
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    /**
     * 생성된 사용자 ID (PK)
     */
    private Long userId;

    /**
     * 로그인 ID
     */
    private String loginId;

    /**
     * 이메일 주소
     */
    private String email;

    /**
     * 닉네임
     */
    private String nickname;

    /**
     * 이름
     */
    private String name;

    /**
     * 생성 시간
     */
    private LocalDateTime createdAt;
}

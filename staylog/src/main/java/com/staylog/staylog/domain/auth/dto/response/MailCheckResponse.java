package com.staylog.staylog.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이메일 인증 확인 응답 DTO
 *
 * @author 이준혁
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "이메일 인증 확인 응답")
public class MailCheckResponse {

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "인증 성공 여부", example = "true")
    private Boolean isVerified;

    /**
     * 인증 성공 응답 생성
     *
     * @param email 이메일 주소
     * @return MailCheckResponse
     */
    public static MailCheckResponse success(String email) {
        return MailCheckResponse.builder()
                .email(email)
                .isVerified(true)
                .build();
    }
}

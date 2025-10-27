package com.staylog.staylog.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 이메일 인증 발송 응답 DTO
 *
 * @author 이준혁
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "이메일 인증 발송 응답")
public class MailSendResponse {

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "인증 코드 만료 시간", example = "2025-10-25T15:30:00")
    private LocalDateTime expiresAt;

    @Schema(description = "인증 코드 유효 기간 (분)", example = "10")
    private Integer validMinutes;

    /**
     * 이메일 발송 응답 생성
     *
     * @param email 이메일 주소
     * @param expiresAt 만료 시간
     * @return MailSendResponse
     */
    public static MailSendResponse of(String email, LocalDateTime expiresAt) {
        return MailSendResponse.builder()
                .email(email)
                .expiresAt(expiresAt)
                .validMinutes(10)
                .build();
    }
}

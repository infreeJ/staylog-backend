package com.staylog.staylog.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Alias("emailVerificationDto")
@Getter
@Setter
public class EmailVerificationDto {
    private Long emailId;
    private String email;
    private String verificationCode;
    private LocalDateTime expiresAt;
    private String isVerified; // 'Y' or 'N'
}

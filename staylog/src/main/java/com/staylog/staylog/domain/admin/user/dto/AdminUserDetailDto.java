package com.staylog.staylog.domain.admin.user.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 *  유저 상세 조회 Dto
 * @author  고영석
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDetailDto {
    private Long userId;
    private String loginId;
    private String password;
    private String nickname;
    private String name;
    private String email;
    private String phone;
    private String profileImage;
    private String birthDate;
    private String gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role;
    private String status;
    private LocalDateTime lastLogin;
    private LocalDateTime withdrawnAt;
}

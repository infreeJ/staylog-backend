package com.staylog.staylog.domain.admin.user.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 *  관리자 유저 목록 조회를 위한 Dto
 *
 * @author 고영석
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserListDto {
    private Long userId;
    private String name;
    private String email;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;

}

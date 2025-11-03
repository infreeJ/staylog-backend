package com.staylog.staylog.domain.admin.user.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 권한 변경
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateRoleResponse {
    private Long userId;
    private String userName;
    private String userRole;

    private LocalDateTime updatedAt;
}


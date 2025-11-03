package com.staylog.staylog.domain.admin.user.dto.request;

import lombok.*;

/**
 * 유저 권한 변경을 위한 객체
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateRoleRequest {
    private String role;
}

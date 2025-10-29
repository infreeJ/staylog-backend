package com.staylog.staylog.domain.admin.user.dto.request;

import lombok.Data;

/**
 * 유저 권한 변경을 위한 객체
 */
@Data
public class AdminUpdateRoleRequest {
    private String role;
}

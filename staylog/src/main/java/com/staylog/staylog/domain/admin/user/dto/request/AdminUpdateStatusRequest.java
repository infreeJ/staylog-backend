package com.staylog.staylog.domain.admin.user.dto.request;

import lombok.Data;

/**
 *  유저 상태 변경을 위한 객체
 */
@Data
public class AdminUpdateStatusRequest {
    private String status;
}

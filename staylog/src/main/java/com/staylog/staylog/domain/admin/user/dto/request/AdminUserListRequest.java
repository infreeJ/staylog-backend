package com.staylog.staylog.domain.admin.user.dto.request;

import com.staylog.staylog.global.common.dto.PageRequest;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserListRequest extends PageRequest {
        private String role; // 권한
        private String status; // 상태

}
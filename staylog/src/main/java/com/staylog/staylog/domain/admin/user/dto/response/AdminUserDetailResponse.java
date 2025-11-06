package com.staylog.staylog.domain.admin.user.dto.response;

import com.staylog.staylog.domain.admin.user.dto.AdminUserListDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관리자 유저 상세 조회 응답 Dto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDetailResponse {
    // 유저 상세 조회 용 (단일 유저만)
    private AdminUserListDto user;


}

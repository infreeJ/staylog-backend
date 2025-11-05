package com.staylog.staylog.domain.admin.user.service;

import com.staylog.staylog.domain.admin.user.dto.AdminUserDetailDto;
import com.staylog.staylog.domain.admin.user.dto.request.AdminUserListRequest;
import com.staylog.staylog.domain.admin.user.dto.response.AdminGetUserDetailResponse;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUpdateRoleResponse;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUpdateStatusResponse;
import com.staylog.staylog.domain.user.dto.UserDto;

public interface AdminUserService {

    /** 유저 목록 조회 (총건수 + 목록 + 페이지 이동번호) */
    AdminGetUserDetailResponse getUsers(int pageNum, AdminUserListRequest req);

    /** 유저 상세 조회 */
    AdminUserDetailDto getUserDetail(Long userId);

    /**
     * 유저 권한 변경
     *
     * @return
     */
    AdminUpdateRoleResponse updateUserRole(Long userId, String role);

    /** 유저 상태 변경 */
    AdminUpdateStatusResponse updateUserStatus(Long userId, String status);

}

package com.staylog.staylog.domain.admin.user.service.impl;


import com.staylog.staylog.domain.admin.user.dto.AdminUserDetailDto;
import com.staylog.staylog.domain.admin.user.dto.AdminUserListDto;
import com.staylog.staylog.domain.admin.user.dto.request.AdminUserListRequest;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUpdateRoleResponse;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUpdateStatusResponse;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUserListResponse;
import com.staylog.staylog.domain.admin.user.mapper.AdminUserMapper;
import com.staylog.staylog.domain.admin.user.service.AdminUserService;
import com.staylog.staylog.global.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;


    @Override
    public AdminUserListResponse getUsers(AdminUserListRequest req) {
        // 전체 개수 조회
        int totalCount = adminUserMapper.countUsers(req);
        // 페이지 계산
        PageResponse page = new PageResponse();
        page.calculate(req, totalCount);
        List<AdminUserListDto> users = adminUserMapper.findUsers(req);


        AdminUserListResponse resp = new AdminUserListResponse();
        resp.setPage(page);
        resp.setUsers(users);

        return resp;
    }

    @Override
    public AdminUserDetailDto getUserDetail(Long userId) {
        return adminUserMapper.getUserDetail(userId);
    }

    @Override
    public AdminUpdateRoleResponse updateUserRole(Long userId, String role) {
        adminUserMapper.updateUserRole(userId, role);

        AdminUserDetailDto user = adminUserMapper.getUserDetail(userId);
        return new AdminUpdateRoleResponse(
                user.getUserId(),
                user.getName(),
                user.getRole(),
                user.getUpdatedAt()
        );
    }

    @Override
    public AdminUpdateStatusResponse updateUserStatus(Long userId, String status) {
        adminUserMapper.updateUserStatus(userId, status);
        AdminUserDetailDto user = adminUserMapper.getUserDetail(userId);
        return new AdminUpdateStatusResponse(
                user.getUserId(),
                user.getName(),
                user.getStatus(),
                user.getUpdatedAt()
        );
    }
}

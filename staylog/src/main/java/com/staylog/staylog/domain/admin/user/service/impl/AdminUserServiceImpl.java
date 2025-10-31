package com.staylog.staylog.domain.admin.user.service.impl;


import com.staylog.staylog.domain.admin.user.dto.AdminUserDetailDto;
import com.staylog.staylog.domain.admin.user.dto.AdminUserListDto;
import com.staylog.staylog.domain.admin.user.dto.request.AdminUserListRequest;
import com.staylog.staylog.domain.admin.user.dto.response.AdminGetUserDetailResponse;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUpdateRoleResponse;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUpdateStatusResponse;
import com.staylog.staylog.domain.admin.user.mapper.AdminUserMapper;
import com.staylog.staylog.domain.admin.user.service.AdminUserService;
import com.staylog.staylog.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;


    @Override
    public AdminGetUserDetailResponse getUsers(int pageNum, AdminUserListRequest req) {

        final int PAGE_ROW_COUNT = 5;     // 한 페이지에 보여줄 행 수
        final int PAGE_DISPLAY_COUNT = 10; // 하단에 보여줄 페이지 버튼 수


        int startRowNum = 1 + (pageNum - 1) * PAGE_ROW_COUNT; // 현재 페이지의 시작 행 번호
        int endRowNum = pageNum * PAGE_ROW_COUNT; // 현재 페이지의 마지막 행 번호


        req.setStartRowNum(startRowNum);
        req.setEndRowNum(endRowNum);


        int totalRow = adminUserMapper.countUsers(req); // 전체 글의 개수 조회
        int totalPageCount = (int) Math.ceil(totalRow / (double) PAGE_ROW_COUNT); // 전체 페이지의 갯수 구하기


        int startPageNum = 1 + ((pageNum - 1) / PAGE_DISPLAY_COUNT) * PAGE_DISPLAY_COUNT;
        int endPageNum = startPageNum + PAGE_DISPLAY_COUNT - 1;
        if (endPageNum > totalPageCount) {
            endPageNum = totalPageCount;
        }


        int prevNum = (startPageNum > 1) ? (startPageNum - 1) : 0;
        int nextNum = (endPageNum < totalPageCount) ? (endPageNum + 1) : 0;


        List<AdminUserListDto> users = adminUserMapper.findUsers(req);


        AdminGetUserDetailResponse resp = new AdminGetUserDetailResponse();
        resp.setUsers(users);
        resp.setTotalCount(totalRow);
        resp.setPrevNum(prevNum);
        resp.setNextNum(nextNum);
        resp.setStartPageNum(startPageNum);
        resp.setEndPageNum(endPageNum);
        resp.setTotalPageCount(totalPageCount);

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

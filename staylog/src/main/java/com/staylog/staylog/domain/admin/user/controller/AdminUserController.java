package com.staylog.staylog.domain.admin.user.controller;

import com.staylog.staylog.domain.admin.user.dto.request.AdminGetUserDetailRequest;
import com.staylog.staylog.domain.admin.user.dto.request.AdminUpdateRoleRequest;
import com.staylog.staylog.domain.admin.user.dto.request.AdminUpdateStatusRequest;
import com.staylog.staylog.domain.admin.user.dto.response.AdminGetUserDetailResponse;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUpdateRoleResponse;
import com.staylog.staylog.domain.admin.user.service.AdminUserService;
import com.staylog.staylog.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 유저 관리 컨트롤러
 * - 유저 목록 조회
 * - 유저 상세 조회
 * - 유저 권한 변경
 * - 유저 상태 변경
 */
@Tag(name = "AdminUserController", description = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 전체 유저 목록
     */
    @Operation(summary = "모든 유저 목록", description = "모든 유저 목록 조회")
    @GetMapping("/admin/users")
    public ResponseEntity<AdminGetUserDetailResponse> getAllUsers(@RequestParam(defaultValue = "1")int pageNum, AdminGetUserDetailRequest.AdminUserSearchReq req) {
        AdminGetUserDetailResponse response = adminUserService.getUsers(pageNum, req);
        return ResponseEntity.ok(response);
    }
    /**
     * 유저 상세 조회
     */
    @Operation(summary = "유저 상세 조회", description = "유저의 정보 조회")
    @GetMapping("/admin/users/{userId}")
    public ResponseEntity<UserDto> getUserDetail(@PathVariable Long userId) {
        UserDto user = adminUserService.getUserDetail(userId);
        return ResponseEntity.ok(user);
    }
    /**
     * 유저 권한 변경 (role)
     */
    @Operation(summary = "유저 권한 변경", description = "user, vip, admin 권한 변경")
    @PatchMapping("/admin/users/{userId}/role")
    public ResponseEntity<AdminUpdateRoleResponse> updateUserRole(@PathVariable Long userId, @RequestBody AdminUpdateRoleRequest req) {
        AdminUpdateRoleResponse res =  adminUserService.updateUserRole(userId, req.getRole());
        return ResponseEntity.ok(res);
    }
    /**
     * 유저 상태 변경 (status)
     */
    @Operation(summary = "유저 상태 변경", description = "활성화, 비활성화 상태 변경")
    @PatchMapping("/admin/users/{userId}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long userId, @RequestBody AdminUpdateStatusRequest req) {
        adminUserService.updateUserStatus(userId, req.getStatus());
        return ResponseEntity.ok().build();
    }
}

package com.staylog.staylog.domain.admin.user.controller;

import com.staylog.staylog.domain.admin.user.dto.AdminUserDetailDto;
import com.staylog.staylog.domain.admin.user.dto.request.AdminUserListRequest;
import com.staylog.staylog.domain.admin.user.dto.request.AdminUpdateRoleRequest;
import com.staylog.staylog.domain.admin.user.dto.request.AdminUpdateStatusRequest;
import com.staylog.staylog.domain.admin.user.dto.response.AdminGetUserDetailResponse;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUpdateRoleResponse;
import com.staylog.staylog.domain.admin.user.dto.response.AdminUpdateStatusResponse;
import com.staylog.staylog.domain.admin.user.service.AdminUserService;
import com.staylog.staylog.domain.user.dto.UserDto;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final MessageUtil messageUtil;
    /**
     * 전체 유저 목록
     */
    @Operation(summary = "모든 유저 목록", description = "모든 유저 목록 조회")
    @GetMapping("/admin/users")
    public ResponseEntity<SuccessResponse<AdminGetUserDetailResponse>> getAllUsers(@RequestParam(defaultValue = "1")int pageNum, AdminUserListRequest req) {
        log.info("모든 유저 목록 조회 : ", req);
        log.info("모든 유저 목록 조회 : ", req.getStatus());

        AdminGetUserDetailResponse response = adminUserService.getUsers(pageNum, req);
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        SuccessResponse<AdminGetUserDetailResponse> success = SuccessResponse.of(code,message,response);
        return ResponseEntity.ok(success);
    }
    /**
     * 유저 상세 조회
     */

    @Operation(summary = "유저 상세 조회", description = "유저의 정보 조회")
    @GetMapping("/admin/users/{userId}")
    public ResponseEntity<SuccessResponse<AdminUserDetailDto>> getUserDetail(@PathVariable Long userId) {
        AdminUserDetailDto user = adminUserService.getUserDetail(userId);
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        SuccessResponse<AdminUserDetailDto> success = SuccessResponse.of(code,message,user);
        return ResponseEntity.ok(success);
    }
    /**
     * 유저 권한 변경 (role)
     */
    @Operation(summary = "유저 권한 변경", description = "user, vip, admin 권한 변경")
    @PatchMapping("/admin/users/{userId}/role")
    public ResponseEntity<SuccessResponse<AdminUpdateRoleResponse>> updateUserRole(@PathVariable Long userId, @RequestBody AdminUpdateRoleRequest req) {
        AdminUpdateRoleResponse response =  adminUserService.updateUserRole(userId, req.getRole());
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        SuccessResponse<AdminUpdateRoleResponse> success = SuccessResponse.of(code,message,response);
        return ResponseEntity.ok(success);
    }
    /**
     * 유저 상태 변경 (status)
     */
    @Operation(summary = "유저 상태 변경", description = "활성화, 비활성화 상태 변경")
    @PatchMapping("/admin/users/{userId}/status")
    public ResponseEntity<SuccessResponse<AdminUpdateStatusResponse>> updateUserStatus(@PathVariable Long userId, @RequestBody AdminUpdateStatusRequest req) {
        AdminUpdateStatusResponse response =  adminUserService.updateUserStatus(userId, req.getStatus());
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        SuccessResponse<AdminUpdateStatusResponse> success = SuccessResponse.of(code,message,response);
        return ResponseEntity.ok(success);
    }
}

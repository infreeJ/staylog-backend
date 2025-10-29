package com.staylog.staylog.domain.mypage.controller;

import com.staylog.staylog.domain.mypage.dto.request.*;
import com.staylog.staylog.domain.mypage.dto.response.*;
import com.staylog.staylog.domain.mypage.service.MypageService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "MypageController", description = "마이페이지 API")
@RestController
@RequestMapping("/v1")
@Slf4j
@AllArgsConstructor
public class MypageController {

    private final MypageService mypageService;
    private final MessageUtil messageUtil;
    
    /**
     * 마이페이지 
     * 	회원정보: 회원정보 조회, 회원정보 수정
     * @Author 오미나 
     * 
     */
    
    /** 회원정보 조회 */
    @Operation(summary = "회원정보 조회", description = "로그인 한 회원정보 조회")
    @GetMapping("/mypage/member")
    public ResponseEntity<SuccessResponse<MemberInfoResponse>> getMember(@RequestParam Long userId) {
        MemberInfoResponse data = mypageService.getMemberInfo(userId);
        String msg = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        return ResponseEntity.ok(SuccessResponse.of(msg, data));
    }

    /** 회원정보 수정 */
    @Operation(summary = "회원정보 수정")
    @PatchMapping("/mypage/member")
    public ResponseEntity<SuccessResponse<Void>> updateMember(@RequestBody @Valid UpdateMemberInfoRequest req) {
        mypageService.updateMemberInfo(req);
        String msg = messageUtil.getMessage(SuccessCode.UPDATED.getMessageKey());
        return ResponseEntity.ok(SuccessResponse.of(msg, null));
    }

    /** 회원 탈퇴(미정) */
//    @Operation(summary = "회원 탈퇴")
//    @DeleteMapping("/mypage/member")
//    public ResponseEntity<SuccessResponse<Void>> deleteMember(@RequestParam Long userId) {
//        mypageService.deleteMember(userId);
//        String msg = messageUtil.getMessage(SuccessCode.DELETED.getMessageKey());
//        return ResponseEntity.ok(SuccessResponse.of(msg, null));
//    }

}


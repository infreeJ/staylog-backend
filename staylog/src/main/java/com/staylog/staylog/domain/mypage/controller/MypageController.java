package com.staylog.staylog.domain.mypage.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.mypage.dto.MemberInfoDto;
import com.staylog.staylog.domain.mypage.dto.response.BookingInfoResponse;
import com.staylog.staylog.domain.mypage.dto.response.ReviewInfoResponse;
import com.staylog.staylog.domain.mypage.service.MypageService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
     * 	예
     * @Author 오미나 
     * 
     */
    
    /** 회원정보 조회 */
    @Operation(summary = "회원정보 조회", description = "로그인 한 회원정보 조회")
    @GetMapping("/mypage/member")
    public ResponseEntity<SuccessResponse<MemberInfoDto>> getMember(@RequestParam Long userId) {
        MemberInfoDto data = mypageService.getMemberInfo(userId);
        String msg = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        return ResponseEntity.ok(SuccessResponse.of(code,msg, data));
    }

    /** 회원정보 수정 */
    @Operation(summary = "회원정보 수정", description = "로그인 한 회원의 정보 수정")
    @PatchMapping("/mypage/member")
    public ResponseEntity<SuccessResponse<Void>> updateMember(@RequestBody @Valid MemberInfoDto dto) {
        mypageService.updateMemberInfo(dto);
        String msg = messageUtil.getMessage(SuccessCode.UPDATED.getMessageKey());
        String code = SuccessCode.UPDATED.name();
        return ResponseEntity.ok(SuccessResponse.of(code,msg, null));
    }

    /** 회원 탈퇴(미정) */
//    @Operation(summary = "회원 탈퇴")
//    @DeleteMapping("/mypage/member")
//    public ResponseEntity<SuccessResponse<Void>> deleteMember(@RequestParam Long userId) {
//        mypageService.deleteMemberInfo(userId);
//        String msg = messageUtil.getMessage(SuccessCode.DELETED.getMessageKey());
//        String code = SuccessCode.DELETED.name();
//        return ResponseEntity.ok(SuccessResponse.of(code,msg, null));
//    }


    /** 예약 내역 (status=upcoming/completed/canceled) */
    @Operation(summary = "예약 내역 조회", description = "status=upcoming/completed/canceled")
    @GetMapping("/mypage/reservations")
    public ResponseEntity<SuccessResponse<List<BookingInfoResponse>>> getBookings(
            @RequestParam Long userId,
            @RequestParam String status) {

        List<BookingInfoResponse> data = mypageService.getBookings(userId, status);
        String msg = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        return ResponseEntity.ok(SuccessResponse.of(code,msg, data));
    }

    /** 리뷰 내역 (type=available/my) */
    @Operation(summary = "리뷰 내역 조회", description = "type=available/my")
    @GetMapping("/mypage/reviews")
    public ResponseEntity<SuccessResponse<List<ReviewInfoResponse>>> getReviews(
            @RequestParam Long userId,
            @RequestParam String type) {

        List<ReviewInfoResponse> data = mypageService.getReviews(userId, type);
        String msg = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        return ResponseEntity.ok(SuccessResponse.of(code, msg, data));
    }

    /** 1:1 문의 내역 (type=inquiry/my) */
    /**
    @Operation(summary = "1:1 문의 내역 조회", description = "type=inquiry/my")
    @GetMapping("/mypage/inquiries")
    public ResponseEntity<SuccessResponse<List<InquiryInfoResponse>>> getInquiries(
            @RequestParam Long userId,
            @RequestParam String type) {

        List<InquiryInfoResponse> data = mypageService.getInquiries(userId, type);
        String msg = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        return ResponseEntity.ok(SuccessResponse.of(code, msg, data));
    }
    */

    /** 1:1 문의 작성 */
    /**
    @Operation(summary = "1:1 문의 작성", description = "type=inquiry/write")
    @PostMapping("/mypage/inquiries")
    public ResponseEntity<SuccessResponse<Void>> writeInquiry(@RequestBody @Valid InquiryWriteRequest req) {
        mypageService.writeInquiry(req);
        String msg = messageUtil.getMessage(SuccessCode.CREATED.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        return ResponseEntity.ok(SuccessResponse.of(code, msg, null));
    }
    */
    
}


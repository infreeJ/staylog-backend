package com.staylog.staylog.domain.coupon.controller;

import com.staylog.staylog.domain.coupon.dto.request.CouponBatchRequest;
import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.dto.request.UseCouponRequest;
import com.staylog.staylog.domain.coupon.dto.response.CouponResponse;
import com.staylog.staylog.domain.coupon.service.CouponService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CouponController", description = "쿠폰 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class CouponController {

    private final CouponService couponService;
    private final MessageUtil messageUtil;

    /**
     * 특정 유저의 사용 가능한 모든 쿠폰 조회
     * @param userId 유저 PK
     * @return List<CouponResponse> 쿠폰 목록
     * @author 이준혁
     */
    @Operation(summary = "사용가능한 쿠폰 목록 조회", description = "로그인한 사용자의 사용가능한 쿠폰 목록을 조회합니다.")
    @GetMapping("/coupon/{userId}/available")
    public ResponseEntity<SuccessResponse<List<CouponResponse>>> getAvailableCouponList(@PathVariable long userId) {
        List<CouponResponse> list = couponService.getAvailableCouponList(userId);

        String message = messageUtil.getMessage(SuccessCode.COUPON_LIST_FIND.getMessageKey());
        String code = SuccessCode.COUPON_LIST_FIND.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, list));
    }

    /**
     * 특정 유저의 이미 사용한 모든 쿠폰 조회
     * @param userId 유저 PK
     * @return List<CouponResponse> 쿠폰 목록
     * @author 이준혁
     */
    @Operation(summary = "사용 불가능한 쿠폰 목록 조회", description = "로그인한 사용자의 사용 불가능한 쿠폰 목록을 조회합니다.")
    @GetMapping("/coupon/{userId}/unavailable")
    public ResponseEntity<SuccessResponse<List<CouponResponse>>> getUnavailableCouponList(@PathVariable long userId) {
        List<CouponResponse> list = couponService.getUnavailableCouponList(userId);

        String message = messageUtil.getMessage(SuccessCode.COUPON_LIST_FIND.getMessageKey());
        String code = SuccessCode.COUPON_LIST_FIND.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, list));
    }

    /**
     * 쿠폰 발급
     * @param couponRequest (userId, couponType)
     * @author 이준혁
     */
    @Operation(summary = "쿠폰 발급", description = "쿠폰을 발급합니다. \n * 시퀀스로 PK를 생성하기 때문에 couponId는 빼고 입력해주세요. \n * expiredAt을 생략하면 무기한 쿠폰이 됩니다.")
    @PostMapping("/coupon")
    public ResponseEntity<SuccessResponse<Void>> saveCoupon(@RequestBody CouponRequest couponRequest) {
        couponService.saveCoupon(couponRequest);

        String message = messageUtil.getMessage(SuccessCode.COUPON_CREATE.getMessageKey());
        String code = SuccessCode.COUPON_CREATE.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }

    /**
     * 모든 유저에게 쿠폰 일괄 발급
     *
     * @param couponBatchRequest couponBatchRequest Dto
     * @author 이준혁
     */
    @Operation(summary = "모든 사용자 쿠폰 일괄 발급", description = "모든 사용자에게 쿠폰을 일괄 발급합니다. \n * expiredAt을 생략하면 무기한 쿠폰이 됩니다.")
    @PostMapping("/coupon/all")
    public ResponseEntity<SuccessResponse<Void>> saveCouponToAllUsers(@RequestBody CouponBatchRequest couponBatchRequest) {
        couponService.saveCouponToAllUsers(couponBatchRequest);

        String message = messageUtil.getMessage(SuccessCode.COUPON_CREATE.getMessageKey());
        String code = SuccessCode.COUPON_CREATE.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }

    /**
     * 쿠폰 사용 처리
     * @param useCouponRequest 쿠폰 PK
     * @author 이준혁
     */
    @Operation(summary = "쿠폰 사용 처리", description = "쿠폰의 사용 여부를 Y로 변경합니다.")
    @PatchMapping("/coupon")
    public ResponseEntity<SuccessResponse<Void>> useCoupon(@RequestBody UseCouponRequest useCouponRequest) {
        couponService.useCoupon(useCouponRequest);

        String message = messageUtil.getMessage(SuccessCode.COUPON_USED.getMessageKey());
        String code = SuccessCode.COUPON_USED.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }



    /**
     * 쿠폰 삭제
     * @param couponId 쿠폰 PK
     * @author 이준혁
     */
    @Operation(summary = "쿠폰 삭제", description = "쿠폰을 삭제합니다.")
    @DeleteMapping("/coupon/{couponId}")
    public ResponseEntity<SuccessResponse<Void>> deleteCoupon(@PathVariable long couponId) {
        couponService.deleteCoupon(couponId);

        String message = messageUtil.getMessage(SuccessCode.COUPON_DELETED.getMessageKey());
        String code = SuccessCode.COUPON_DELETED.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }
}






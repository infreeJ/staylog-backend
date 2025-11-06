package com.staylog.staylog.domain.refund.controller;

import com.staylog.staylog.domain.refund.dto.request.RequestRefundRequest;
import com.staylog.staylog.domain.refund.dto.response.RefundDetailResponse;
import com.staylog.staylog.domain.refund.dto.response.RefundResultResponse;
import com.staylog.staylog.domain.refund.service.RefundService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import com.staylog.staylog.global.security.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 환불 Controller
 */
@RestController
@RequestMapping("/v1/refunds")
@RequiredArgsConstructor
@Slf4j
public class RefundController {

    private final RefundService refundService;
    private final MessageUtil messageUtil;

    /**
     * 환불 요청
     * - 예약 취소 및 환불 생성
     */
    @PostMapping
    public ResponseEntity<SuccessResponse<RefundDetailResponse>> requestRefund(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody RequestRefundRequest request
    ) {
        Long userId = user.getUserId();
        log.info("환불 요청: userId={}, bookingId={}", userId, request.getBookingId());

        RefundDetailResponse response = refundService.requestRefund(userId, request);

        return ResponseEntity.ok(
                SuccessResponse.of(
                        SuccessCode.REFUND_REQUEST_SUCCESS.getCode(),
                        messageUtil.getMessage(SuccessCode.REFUND_REQUEST_SUCCESS.getMessageKey()),
                        response
                )
        );
    }

    /**
     * 환불 처리 (관리자용)
     * - Toss API 호출하여 실제 환불 처리
     */
    @PostMapping("/{refundId}/process")
    public ResponseEntity<SuccessResponse<RefundResultResponse>> processRefund(
            @PathVariable Long refundId
    ) {
        log.info("환불 처리 요청: refundId={}", refundId);

        RefundResultResponse response = refundService.processRefund(refundId);

        return ResponseEntity.ok(
                SuccessResponse.of(
                        SuccessCode.REFUND_PROCESS_SUCCESS.getCode(),
                        messageUtil.getMessage(SuccessCode.REFUND_PROCESS_SUCCESS.getMessageKey()),
                        response
                )
        );
    }

    /**
     * 환불 조회
     */
    @GetMapping("/{refundId}")
    public ResponseEntity<SuccessResponse<RefundDetailResponse>> getRefund(
            @PathVariable Long refundId
    ) {
        log.info("환불 조회 요청: refundId={}", refundId);

        RefundDetailResponse response = refundService.getRefund(refundId);

        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();

        return ResponseEntity.ok(SuccessResponse.of(code, message, response));
    }
}

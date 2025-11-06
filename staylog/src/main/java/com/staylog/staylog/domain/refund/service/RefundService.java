package com.staylog.staylog.domain.refund.service;

import com.staylog.staylog.domain.refund.dto.request.RequestRefundRequest;
import com.staylog.staylog.domain.refund.dto.response.RefundDetailResponse;
import com.staylog.staylog.domain.refund.dto.response.RefundResultResponse;

/**
 * 환불 서비스 인터페이스
 */
public interface RefundService {

    /**
     * 환불 요청
     * - 예약 취소 및 환불 생성
     * - 환불 정책 계산
     * @param userId 사용자 ID
     * @param request 환불 요청
     * @return 환불 상세 정보
     */
    RefundDetailResponse requestRefund(Long userId, RequestRefundRequest request);

    /**
     * 환불 처리 (Toss API 호출)
     * - Toss 결제 취소 API 호출
     * - 성공: REFUND(COMPLETED) + PAYMENT(PAY_REFUND) + RESERVATION(RES_REFUNDED)
     * - 실패: REFUND(FAILED)
     * @param refundId 환불 ID
     * @return 환불 결과
     */
    RefundResultResponse processRefund(Long refundId);

    /**
     * 환불 조회
     * @param refundId 환불 ID
     * @return 환불 상세 정보
     */
    RefundDetailResponse getRefund(Long refundId);
}

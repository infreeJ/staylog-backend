package com.staylog.staylog.domain.refund.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 환불 Mapper 인터페이스
 */
@Mapper
public interface RefundMapper {

    /**
     * 환불 생성
     * @param params 환불 정보 (paymentId, bookingId, totalAmount, refundAmount, refundType, refundReason, status)
     */
    void insertRefund(Map<String, Object> params);

    /**
     * 환불 조회 (refundId)
     * @param refundId 환불 ID
     * @return 환불 정보
     */
    Map<String, Object> findRefundById(@Param("refundId") Long refundId);

    /**
     * 환불 조회 (paymentId)
     * @param paymentId 결제 ID
     * @return 환불 정보
     */
    Map<String, Object> findRefundByPaymentId(@Param("paymentId") Long paymentId);

    /**
     * 환불 상태 업데이트
     * @param refundId 환불 ID
     * @param status 변경할 상태
     */
    void updateRefundStatus(@Param("refundId") Long refundId,
                            @Param("status") String status);

    /**
     * 환불 완료 처리
     * @param refundId 환불 ID
     * @param status 최종 상태 (COMPLETED)
     */
    void updateRefundCompletion(@Param("refundId") Long refundId,
                                @Param("status") String status);

    /**
     * 환불 실패 처리
     * @param refundId 환불 ID
     * @param status 최종 상태 (FAILED/REJECTED)
     * @param failureReason 실패 사유
     */
    void updateRefundFailure(@Param("refundId") Long refundId,
                             @Param("status") String status,
                             @Param("failureReason") String failureReason);
}

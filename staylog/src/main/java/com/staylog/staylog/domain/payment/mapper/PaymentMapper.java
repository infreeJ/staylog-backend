package com.staylog.staylog.domain.payment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 결제 Mapper 인터페이스
 */
@Mapper
public interface PaymentMapper {

    /**
     * 결제 생성
     * @param params 결제 정보 (status, amount, method, bookingId, paymentKey)
     */
    void insertPayment(Map<String, Object> params);

    /**
     * 결제 조회 (paymentId)
     * @param paymentId 결제 ID
     * @return 결제 정보
     */
    Map<String, Object> findPaymentById(@Param("paymentId") Long paymentId);

    /**
     * 결제 조회 (bookingId)
     * @param bookingId 예약 ID
     * @return 결제 정보
     */
    Map<String, Object> findPaymentByBookingId(@Param("bookingId") Long bookingId);

    /**
     * 결제 조회 (paymentKey)
     * @param paymentKey Toss 결제 키
     * @return 결제 정보
     */
    Map<String, Object> findPaymentByPaymentKey(@Param("paymentKey") String paymentKey);

    /**
     * 결제 상태 업데이트 (승인 성공 시)
     * @param paymentId 결제 ID
     * @param status 변경할 상태 (PAY_PAID, PAY_FAILED, etc.)
     * @param paymentKey Toss 결제 키
     * @param lastTransactionKey 마지막 트랜잭션 키
     */
    void updatePaymentStatus(@Param("paymentId") Long paymentId,
                             @Param("status") String status,
                             @Param("paymentKey") String paymentKey,
                             @Param("lastTransactionKey") String lastTransactionKey);

    /**
     * 결제 실패 정보 업데이트
     * @param paymentId 결제 ID
     * @param status 변경할 상태 (PAY_FAILED)
     * @param failureReason 실패 사유
     */
    void updatePaymentFailure(@Param("paymentId") Long paymentId,
                              @Param("status") String status,
                              @Param("failureReason") String failureReason);
}

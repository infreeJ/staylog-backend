package com.staylog.staylog.domain.payment.service;

import com.staylog.staylog.domain.payment.dto.request.ConfirmPaymentRequest;
import com.staylog.staylog.domain.payment.dto.request.PreparePaymentRequest;
import com.staylog.staylog.domain.payment.dto.response.PaymentResultResponse;
import com.staylog.staylog.domain.payment.dto.response.PreparePaymentResponse;

/**
 * 결제 서비스 인터페이스
 */
public interface PaymentService {

    /**
     * 결제 준비
     * - 예약 상태 검증 (PENDING & 5분 이내)
     * - 결제 생성 (READY 상태)
     *
     * @param request 결제 준비 요청
     * @return 결제 준비 정보 (프론트엔드에서 Toss SDK 초기화에 필요)
     */
    PreparePaymentResponse preparePayment(PreparePaymentRequest request);

    /**
     * 결제 승인
     * - Toss API 호출
     * - 성공 시: PAYMENT(PAID) + RESERVATION(CONFIRMED)
     * - 실패 시: 보상 트랜잭션 (PAYMENT(FAILED) + RESERVATION(CANCELED))
     *
     * @param request 결제 승인 요청
     * @return 결제 결과
     */
    PaymentResultResponse confirmPayment(ConfirmPaymentRequest request);
}

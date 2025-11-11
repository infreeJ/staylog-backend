package com.staylog.staylog.domain.payment.service.impl;

import com.staylog.staylog.domain.booking.dto.response.BookingDetailResponse;
import com.staylog.staylog.domain.booking.entity.Booking;
import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.booking.service.BookingService;
import com.staylog.staylog.domain.coupon.dto.response.CouponResponse;
import com.staylog.staylog.domain.coupon.service.CouponService;
import com.staylog.staylog.domain.payment.dto.request.ConfirmPaymentRequest;
import com.staylog.staylog.domain.payment.dto.request.PreparePaymentRequest;
import com.staylog.staylog.domain.payment.dto.response.PaymentResultResponse;
import com.staylog.staylog.domain.payment.dto.response.PreparePaymentResponse;
import com.staylog.staylog.domain.payment.entity.Payment;
import com.staylog.staylog.domain.payment.mapper.PaymentMapper;
import com.staylog.staylog.domain.payment.service.PaymentCompensationService;
import com.staylog.staylog.domain.payment.service.PaymentService;
import com.staylog.staylog.external.toss.client.TossPaymentClient;
import com.staylog.staylog.external.toss.config.TossPaymentsConfig;
import com.staylog.staylog.external.toss.dto.request.TossConfirmRequest;
import com.staylog.staylog.external.toss.dto.request.TossVirtualAccountRequest;
import com.staylog.staylog.external.toss.dto.response.TossPaymentResponse;
import com.staylog.staylog.external.toss.dto.response.TossVirtualAccountResponse;
import com.staylog.staylog.external.toss.dto.response.VirtualAccount;
import com.staylog.staylog.global.constant.PaymentStatus;
import com.staylog.staylog.global.constant.ReservationStatus;
import com.staylog.staylog.global.event.PaymentConfirmEvent;
import com.staylog.staylog.global.exception.custom.booking.BookingNotFoundException;
import com.staylog.staylog.global.exception.custom.payment.PaymentAmountMismatchException;
import com.staylog.staylog.global.exception.custom.payment.PaymentFailedException;
import com.staylog.staylog.global.exception.custom.payment.TossApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 결제 서비스 구현
 * - 결제 준비 (READY 상태)
 * - 결제 승인 (Toss API 호출)
 * - 보상 트랜잭션 (실패 시 롤백)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;
    private final TossPaymentClient tossPaymentClient;
    private final TossPaymentsConfig tossConfig;
    private final PaymentCompensationService compensationService;
    private final CouponService couponService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 결제 준비
     * - 예약 상태 검증 (PENDING & 5분 이내)
     * - 결제 생성 (READY 상태)
     */
    @Override
    @Transactional
    public PreparePaymentResponse preparePayment(PreparePaymentRequest request) {
        log.info("결제 준비 시작: bookingId={}, amount={}", request.getBookingId(), request.getAmount());

        // 1. 예약 상태 검증 (PENDING & 5분 이내)
        bookingService.validateBookingPending(request.getBookingId());

        // 2. 예약 정보 조회
        BookingDetailResponse booking = bookingMapper.findBookingById(request.getBookingId());
        if (booking == null) {
            throw new BookingNotFoundException(request.getBookingId());
        }

        Long bookingAmount = booking.getAmount();
        String bookingNum = booking.getBookingNum();
        String guestName = booking.getGuestName();
        Long userId = booking.getUserId();

        // 3. 금액 검증
        if (!bookingAmount.equals(request.getAmount())) {
            log.error("결제 금액 불일치: 예약금액={}, 요청금액={}", bookingAmount, request.getAmount());
            throw new PaymentAmountMismatchException(bookingAmount, request.getAmount());
        }

        // 4. 🆕 쿠폰 할인 계산 (couponId가 있는 경우)
        Long originalAmount = request.getAmount();  // 할인 전 금액
        Long discountAmount = 0L;
        Long finalAmount = originalAmount;
        Long couponId = request.getCouponId();

        if (couponId != null) {
            // 쿠폰 검증 메서드 호출
            CouponResponse availableCoupon = couponService.validateCoupon(userId, couponId);
            
            // 할인금액 계산 및 최종금액 계산
            discountAmount = couponService.calculateCouponDiscount(originalAmount, availableCoupon.getDiscount());
            finalAmount = originalAmount - discountAmount;

            // 최종 금액은 0원 이상이어야 함
            if (finalAmount < 0) {
                finalAmount = 0L;
            }

            // ✅ RESERVATION.FINAL_AMOUNT 업데이트
            bookingMapper.updateFinalAmount(request.getBookingId(), finalAmount);

            log.info("쿠폰 할인 적용: 쿠폰ID={}, 원래금액={}, 할인액={}, 최종금액={}",
                    couponId, originalAmount, discountAmount, finalAmount);
        } else {
            // 쿠폰 미사용 시에도 FINAL_AMOUNT 업데이트 (AMOUNT와 동일)
            bookingMapper.updateFinalAmount(request.getBookingId(), finalAmount);
        }

        // 5. 계좌이체인 경우 만료 시간 연장 (5분 → 7일)
        if ("TRANSFER".equals(request.getMethod())) {
            LocalDateTime newExpiresAt = LocalDateTime.now().plusDays(7);
            bookingMapper.updateExpiresAt(request.getBookingId(), newExpiresAt);
            log.info("계좌이체 예약 만료 시간 연장: bookingId={}, expiresAt={}", request.getBookingId(), newExpiresAt);
        }

        // 7. 결제 생성 (READY 상태, 쿠폰 정보 포함)
        Payment payment = Payment.builder()
                .status(PaymentStatus.PAY_READY.getCode())
                .amount(finalAmount) //할인 후 최종금액
                .method(request.getMethod())
                .bookingId(request.getBookingId())
                .paymentKey(null) // READY상태일 떄는 토스에서 보내주는 결제 키 없음 (Toss 승인하면 업데이트)
                .couponId(couponId) // 쿠폰
                .originalAmount(originalAmount) //할인 전 금액
                .discountAmount(discountAmount) // 할인 금액
                .requestedAt(OffsetDateTime.now())
                .build();

        paymentMapper.insertPayment(payment);

        // 8. 생성된 결제 조회
        Long paymentId = payment.getPaymentId();

        log.info("결제 준비 완료: paymentId={}, orderId={}, method={}", paymentId, bookingNum, request.getMethod());

        // 9. PreparePaymentResponse 생성 (가상계좌 정보는 Toss SDK가 처리)
        return PreparePaymentResponse.builder()
                .paymentId(paymentId)
                .orderId(bookingNum)  // Toss에 전달할 주문번호
                .amount(finalAmount)  // ✅ 할인 후 최종 금액 반환
                .method(request.getMethod())
                .clientKey(tossConfig.getClientKey())  // 프론트엔드용
                .customerName(guestName)
                .build();
    }

    /**
     * 결제 승인
     * - Toss API 호출
     * - 성공: PAYMENT(PAID) + RESERVATION(CONFIRMED)
     * - 실패: 보상 트랜잭션 (PAYMENT(FAILED) + RESERVATION(CANCELED))
     */
    @Override
    @Transactional
    public PaymentResultResponse confirmPayment(ConfirmPaymentRequest request) {
        log.info("결제 승인 시작: paymentKey={}, orderId={}, amount={}",
                request.getPaymentKey(), request.getOrderId(), request.getAmount());

        // 1. 예약 조회 (orderId = bookingNum)
        Booking booking = bookingMapper.findBookingByBookingNum(request.getOrderId());
        if (booking == null) {
            throw new PaymentFailedException("예약을 찾을 수 없습니다: " + request.getOrderId());
        }

        Long bookingId = booking.getBookingId();

        // 2. 결제 조회 (금액 검증 전에 먼저 조회)
        Payment payment = paymentMapper.findPaymentByBookingId(bookingId);
        if (payment == null) {
            throw new PaymentFailedException("결제 정보를 찾을 수 없습니다");
        }

        // 이미 완료된 결제일 경우 결제 정보를 즉시 리턴
        if(payment.getStatus().equals("PAY_PAID")) {
            log.info("이미 완료된 결제입니다.: paymentId={}, bookingId={}, couponId={}", payment.getPaymentId(), payment.getBookingId(), payment.getCouponId());
            return PaymentResultResponse.builder()
                    .paymentId(payment.getPaymentId())
                    .paymentKey(payment.getPaymentKey())
                    .orderId(request.getOrderId())
                    .amount(payment.getAmount())
                    .method(payment.getMethod())
                    .paymentStatus(PaymentStatus.PAY_PAID.getCode())
                    .reservationStatus(ReservationStatus.RES_CONFIRMED.getCode())
                    .requestedAt(payment.getRequestedAt())
                    .approvedAt(payment.getApprovedAt())
                    .build();
        }

        // 3. ✅ 금액 검증 (PAYMENT.AMOUNT와 비교 - 할인 후 최종 금액)
        if (!payment.getAmount().equals(request.getAmount())) {
            log.error("결제 금액 불일치: 결제금액(할인후)={}, Toss요청금액={}",
                    payment.getAmount(), request.getAmount());

            // 보상 트랜잭션 실행 (독립 트랜잭션)
            compensationService.compensateFailedPayment(bookingId, "결제 금액 불일치");

            throw new PaymentAmountMismatchException(payment.getAmount(), request.getAmount());
        }

        Long paymentId = payment.getPaymentId();

        try {
            // 4. Toss API 결제 승인 호출
            TossConfirmRequest tossRequest = TossConfirmRequest.builder()
                    .paymentKey(request.getPaymentKey())
                    .orderId(request.getOrderId())
                    .amount(request.getAmount())
                    .build();

            TossPaymentResponse tossResponse = tossPaymentClient.confirm(tossRequest);

            // 토스에서 받은 method가 "가상계좌"인 경우 내부적으로 VIRTUAL_ACCOUNT로 매핑
            String internalMethod = "가상계좌".equals(tossResponse.getMethod())
                    ? "VIRTUAL_ACCOUNT"
                    : tossResponse.getMethod();

            // 가상계좌인 경우 만료 시간 연장 (5분 → 24시간)
            if ("가상계좌".equals(tossResponse.getMethod())) {
                LocalDateTime newExpiresAt = LocalDateTime.now().plusHours(24);
                bookingMapper.updateExpiresAt(bookingId, newExpiresAt);
                log.info("가상계좌 예약 만료 시간 연장: bookingId={}, expiresAt={}", bookingId, newExpiresAt);
            }

            // 5. 가상계좌 정보가 있으면 저장
            if (tossResponse.getVirtualAccount() != null) {
                VirtualAccount va = tossResponse.getVirtualAccount();
                paymentMapper.updateVirtualAccountInfo(
                        paymentId,
                        va.getBank(),
                        va.getAccountNumber(),
                        va.getCustomerName(),
                        va.getDueDate()
                );
                log.info("가상계좌 정보 저장: bank={}, accountNumber={}, dueDate={}",
                         va.getBank(), va.getAccountNumber(), va.getDueDate());
            }

            // 6. 결제 상태 업데이트
            // 가상계좌는 READY 상태 유지 (입금 대기), 일반 결제는 PAID
            // 결제 상태
            String paymentStatus = "VIRTUAL_ACCOUNT".equals(internalMethod)
                    ? PaymentStatus.PAY_READY.getCode()
                    : PaymentStatus.PAY_PAID.getCode();

            paymentMapper.updatePaymentApproved(
                    paymentId,
                    paymentStatus,
                    tossResponse.getPaymentKey(),
                    tossResponse.getLastTransactionKey()
            );

            // 7. 예약 상태 업데이트
            // 가상계좌는 PENDING 유지 (입금 대기), 일반 결제는 CONFIRMED
            String bookingStatus = "VIRTUAL_ACCOUNT".equals(internalMethod)
                    ? ReservationStatus.RES_PENDING.getCode()
                    : ReservationStatus.RES_CONFIRMED.getCode();

            bookingMapper.updateBookingStatus(bookingId, bookingStatus);

            log.info("결제 승인 성공: paymentId={}, bookingId={}, paymentStatus={}, bookingStatus={} , method = {}",
                     paymentId, bookingId, paymentStatus, bookingStatus, tossResponse.getMethod());

            // ============ 결제 완료 이벤트 발행(알림 전송 / 쿠폰 사용처리) =============
            PaymentConfirmEvent event = new PaymentConfirmEvent(paymentId, bookingId, tossResponse.getTotalAmount(), payment.getCouponId());
            eventPublisher.publishEvent(event);
            // ==========================================================



            return PaymentResultResponse.builder()
                    .paymentId(paymentId)
                    .paymentKey(tossResponse.getPaymentKey())
                    .orderId(tossResponse.getOrderId())
                    .bookingId(bookingId)
                    .amount(tossResponse.getTotalAmount())
                    .method(tossResponse.getMethod())
                    .paymentStatus(paymentStatus.equals(tossResponse.getMethod())
                            ? PaymentStatus.PAY_READY.getCode()
                            : PaymentStatus.PAY_PAID.getCode())
                    .reservationStatus(bookingStatus.equals(tossResponse.getMethod())
                            ? ReservationStatus.RES_PENDING.getCode()
                            : ReservationStatus.RES_CONFIRMED.getCode())
                    .requestedAt(payment.getRequestedAt())
                    .approvedAt(tossResponse.getApprovedAt())
                    .virtualAccount(tossResponse.getVirtualAccount())
                    .build();

        } catch (TossApiException e) {
            // 6. 실패: 보상 트랜잭션 (PAYMENT(FAILED) + RESERVATION(CANCELED))
            log.error("Toss 결제 승인 실패: {}", e.getMessage(), e);

            // 독립적인 트랜잭션으로 보상 실행 (REQUIRES_NEW)
            compensationService.compensateFailedPayment(bookingId, e.getTossErrorMessage());

            throw e;  // 호출자에게 예외 전파
        } catch (Exception e) {
            // 7. 기타 예외: 보상 트랜잭션
            log.error("결제 승인 중 예외 발생: {}", e.getMessage(), e);

            // 독립적인 트랜잭션으로 보상 실행 (REQUIRES_NEW)
            compensationService.compensateFailedPayment(bookingId, "결제 처리 중 오류 발생");

            throw new PaymentFailedException("결제 처리 중 오류가 발생했습니다");
        }
    }

//    /**
//     * 가상계좌 수동 입금 확인 (개발/테스트용)
//     * Swagger에서 수동으로 입금 완료 처리
//     */
//    @Override
//    @Transactional
//    public PaymentResultResponse manualDepositConfirm(Long paymentId) {
//        log.info("[수동 입금 확인] paymentId={}", paymentId);
//
//        // 1. Payment 조회
//        Payment payment = paymentMapper.findPaymentById(paymentId);
//        if (payment == null) {
//            throw new PaymentFailedException("결제 정보를 찾을 수 없습니다: paymentId=" + paymentId);
//        }
//
//        // 2. 가상계좌인지 확인
//        if (!"VIRTUAL_ACCOUNT".equals(payment.getMethod())) {
//            throw new PaymentFailedException("가상계좌 결제가 아닙니다: method=" + payment.getMethod());
//        }
//
//        // 3. 이미 입금 완료된 경우
//        if (PaymentStatus.PAY_PAID.getCode().equals(payment.getStatus())) {
//            log.warn("[수동 입금 확인] 이미 입금 완료된 결제입니다: paymentId={}", paymentId);
//            throw new PaymentFailedException("이미 입금 완료된 결제입니다");
//        }
//
//        Long bookingId = payment.getBookingId();
//
//        // 4. Payment 상태 업데이트: READY → PAID
//        paymentMapper.updateVirtualAccountDeposit(
//                paymentId,
//                PaymentStatus.PAY_PAID.getCode(),
//                OffsetDateTime.now()
//        );
//
//        // 5. Booking 상태 업데이트: PENDING → CONFIRMED
//        bookingMapper.updateBookingStatus(bookingId, ReservationStatus.RES_CONFIRMED.getCode());
//
//        log.info("[수동 입금 확인 완료] paymentId={}, bookingId={}", paymentId, bookingId);
//
//        // 6. 🎉 결제 완료 이벤트 발행 (알림 전송 / 쿠폰 사용처리)
//        PaymentConfirmEvent event = new PaymentConfirmEvent(
//                paymentId,
//                bookingId,
//                payment.getAmount(),
//                payment.getCouponId()
//        );
//        eventPublisher.publishEvent(event);
//
//        // 7. 응답 생성
//        Payment updatedPayment = paymentMapper.findPaymentById(paymentId);
//
//        return PaymentResultResponse.builder()
//                .paymentId(paymentId)
//                .bookingId(bookingId)
//                .orderId(bookingMapper.findBookingById(bookingId).getBookingNum())
//                .amount(updatedPayment.getAmount())
//                .method(updatedPayment.getMethod())
//                .paymentStatus(PaymentStatus.PAY_PAID.getCode())
//                .reservationStatus(ReservationStatus.RES_CONFIRMED.getCode())
//                .requestedAt(updatedPayment.getRequestedAt())
//                .approvedAt(updatedPayment.getApprovedAt())
//                .build();
//    }
}

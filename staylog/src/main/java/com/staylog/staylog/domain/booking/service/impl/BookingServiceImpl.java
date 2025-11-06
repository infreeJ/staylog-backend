package com.staylog.staylog.domain.booking.service.impl;

import com.staylog.staylog.domain.booking.dto.request.CreateBookingRequest;
import com.staylog.staylog.domain.booking.dto.response.BookingDetailResponse;
import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.booking.service.BookingService;
import com.staylog.staylog.global.exception.custom.booking.BookingExpiredException;
import com.staylog.staylog.global.exception.custom.booking.BookingNotFoundException;
import com.staylog.staylog.global.exception.custom.booking.BookingNotPendingException;
import com.staylog.staylog.global.exception.custom.booking.RoomNotAvailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 예약 서비스 구현
 * - 예약 생성 (PENDING 상태)
 * - 객실 가용성 검증
 * - 5분 타임아웃 처리
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;

    /**
     * 예약 생성
     * - 객실 가용성 검증
     * - 예약 생성 (PENDING 상태)
     * - 5분 타임아웃 설정
     */
    @Override
    @Transactional
    public BookingDetailResponse createBooking(Long userId, String guestName, CreateBookingRequest request) {
        log.info("예약 생성 시작: userId={}, guestName={}, roomId={}, checkIn={}, checkOut={}",
                userId, guestName, request.getRoomId(), request.getCheckIn(), request.getCheckOut());

        // 1. 객실 가용성 체크 (중복 예약 확인)
        int conflictCount = bookingMapper.checkRoomAvailability(
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut()
        );

        if (conflictCount > 0) {
            log.warn("객실 예약 불가: roomId={}, 중복 예약 {}건", request.getRoomId(), conflictCount);
            throw new RoomNotAvailableException();
        }

        // 2. 주문번호 생성 (UUID 기반 - Toss에 전달)
        String bookingNum = generateBookingNum();

        // 3. 총 인원 수 계산
        Integer adults = request.getAdults() != null ? request.getAdults() : 1;
        Integer children = request.getChildren() != null ? request.getChildren() : 0;
        Integer infants = request.getInfants() != null ? request.getInfants() : 0;
        Integer totalGuestCount = adults + children + infants;

        // 4. 만료 시간 계산 (결제 수단에 따라 다름)
        LocalDateTime expiresAt = calculateExpiresAt(request.getPaymentMethod());

        // 5. 예약 생성
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("roomId", request.getRoomId());
        params.put("bookingNum", bookingNum);
        params.put("amount", request.getAmount());
        params.put("checkIn", request.getCheckIn());
        params.put("checkOut", request.getCheckOut());
        params.put("status", "RES_PENDING");  // PENDING 상태로 생성
        params.put("guestName", guestName);  // JWT에서 가져온 닉네임
        params.put("adults", adults);
        params.put("children", children);
        params.put("infants", infants);
        params.put("totalGuestCount", totalGuestCount);
        params.put("expiresAt", expiresAt);  // 만료 시간

        bookingMapper.insertBooking(params);

        // 5. 생성된 예약 조회 (useGeneratedKeys로 ID 받아옴)
        Long bookingId = (Long) params.get("bookingId");
        Map<String, Object> booking = bookingMapper.findBookingById(bookingId);

        if (booking == null) {
            throw new RuntimeException("예약 생성 실패");
        }

        log.info("예약 생성 완료: bookingId={}, bookingNum={}", bookingId, bookingNum);

        return mapToBookingDetailResponse(booking);
    }

    /**
     * 예약 조회
     */
    @Override
    @Transactional(readOnly = true)
    public BookingDetailResponse getBooking(Long bookingId) {
        Map<String, Object> booking = bookingMapper.findBookingById(bookingId);

        if (booking == null) {
            throw new BookingNotFoundException(bookingId);
        }

        return mapToBookingDetailResponse(booking);
    }

    /**
     * 예약 상태가 PENDING인지 검증 (결제 준비 전)
     * - 만료 체크 (EXPIRES_AT 기준)
     * - PENDING 상태 체크
     */
    @Override
    @Transactional(readOnly = true)
    public void validateBookingPending(Long bookingId) {
        Map<String, Object> booking = bookingMapper.findBookingById(bookingId);

        if (booking == null) {
            throw new BookingNotFoundException(bookingId);
        }

        String status = (String) booking.get("status");

        // 만료 체크 (EXPIRES_AT 기준)
        LocalDateTime expiresAt = (LocalDateTime) booking.get("expiresAt");
        if (expiresAt.isBefore(LocalDateTime.now())) {
            log.warn("예약 만료: bookingId={}, expiresAt={}", bookingId, expiresAt);
            throw new BookingExpiredException(bookingId);
        }

        // PENDING 상태 체크
        if (!"RES_PENDING".equals(status)) {
            log.warn("예약이 PENDING 상태가 아님: bookingId={}, status={}", bookingId, status);
            throw new BookingNotPendingException(status);
        }
    }

    /**
     * 예약 상태 업데이트
     */
    @Override
    @Transactional
    public void updateBookingStatus(Long bookingId, String status) {
        bookingMapper.updateBookingStatus(bookingId, status);
        log.info("예약 상태 업데이트: bookingId={}, status={}", bookingId, status);
    }

    /**
     * 만료된 예약 자동 취소
     * - EXPIRES_AT이 현재 시간보다 이전인 PENDING 예약을 CANCELED로 변경
     * - 결제 수단에 따라 만료 시간이 다름 (카드 5분, 가상계좌 7일 등)
     */
    @Override
    @Transactional
    public int cancelExpiredBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Map<String, Object>> expiredBookings = bookingMapper.findExpiredBookings(now);

        if (expiredBookings.isEmpty()) {
            return 0;
        }

        int canceledCount = 0;
        for (Map<String, Object> booking : expiredBookings) {
            Long bookingId = ((Number) booking.get("bookingId")).longValue();
            String bookingNum = (String) booking.get("bookingNum");

            try {
                bookingMapper.updateBookingStatus(bookingId, "RES_CANCELED");
                log.info("만료된 예약 취소: bookingId={}, bookingNum={}", bookingId, bookingNum);
                canceledCount++;
            } catch (Exception e) {
                log.error("예약 취소 실패: bookingId={}, error={}", bookingId, e.getMessage(), e);
            }
        }

        return canceledCount;
    }

    /**
     * 주문번호 생성
     * 형식: ORDER_{timestamp}_{UUID 앞 8자리}
     */
    private String generateBookingNum() {
        return "ORDER_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 만료 시간 계산
     *
     * 예약 생성 시점에는 결제 수단을 알 수 없으므로 기본값 5분으로 설정
     * 결제 준비 시점에 결제 수단에 따라 만료 시간을 연장함
     *
     * @param paymentMethod 결제 수단 (사용 안 함, 하위 호환성 유지용)
     * @return 만료 시간 (5분 후)
     */
    private LocalDateTime calculateExpiresAt(String paymentMethod) {
        // 모든 예약 5분으로 생성
        // 계좌이체는 결제 준비 시점에 7일로 연장
        return LocalDateTime.now().plusMinutes(5);
    }

    /**
     * Map -> BookingDetailResponse 변환
     */
    private BookingDetailResponse mapToBookingDetailResponse(Map<String, Object> booking) {
        LocalDateTime createdAt = (LocalDateTime) booking.get("createdAt");

        return BookingDetailResponse.builder()
                .bookingId(((Number) booking.get("bookingId")).longValue())
                .bookingNum((String) booking.get("bookingNum"))
                .userId(((Number) booking.get("userId")).longValue())
                .roomId(((Number) booking.get("roomId")).longValue())
                .roomName((String) booking.get("roomName"))
                .accommodationName((String) booking.get("accommodationName"))
                .checkIn((LocalDate) booking.get("checkIn"))
                .checkOut((LocalDate) booking.get("checkOut"))
                .amount(((Number) booking.get("amount")).longValue())
                .status((String) booking.get("status"))
                .guestName((String) booking.get("guestName"))
                .adults(booking.get("adults") != null ? ((Number) booking.get("adults")).intValue() : null)
                .children(booking.get("children") != null ? ((Number) booking.get("children")).intValue() : null)
                .infants(booking.get("infants") != null ? ((Number) booking.get("infants")).intValue() : null)
                .totalGuestCount(booking.get("totalGuestCount") != null ? ((Number) booking.get("totalGuestCount")).intValue() : null)
                .createdAt(createdAt)
                .updatedAt((LocalDateTime) booking.get("updatedAt"))
                .expiresAt(createdAt.plusMinutes(5))  // 5분 타임아웃
                .paymentId(booking.get("paymentId") != null ? ((Number) booking.get("paymentId")).longValue() : null)
                .build();
    }
}

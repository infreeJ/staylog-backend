package com.staylog.staylog.domain.booking.service;

import com.staylog.staylog.domain.booking.dto.request.CreateBookingRequest;
import com.staylog.staylog.domain.booking.dto.response.BookingDetailResponse;

/**
 * 예약 서비스 인터페이스
 */
public interface BookingService {

    /**
     * 예약 생성
     * @param userId 사용자 ID
     * @param guestName 예약자 이름 (JWT에서 추출한 nickname)
     * @param request 예약 생성 요청
     * @return 생성된 예약 정보
     */
    BookingDetailResponse createBooking(Long userId, String guestName, CreateBookingRequest request);

    /**
     * 예약 조회
     * @param bookingId 예약 ID
     * @return 예약 정보
     */
    BookingDetailResponse getBooking(Long bookingId);

    /**
     * 예약 상태가 PENDING인지 검증 (결제 준비 전)
     * @param bookingId 예약 ID
     * @throws com.staylog.staylog.global.exception.custom.booking.BookingExpiredException 예약 만료 시
     * @throws com.staylog.staylog.global.exception.custom.booking.BookingNotPendingException 예약이 PENDING 상태가 아닐 시
     */
    void validateBookingPending(Long bookingId);

    /**
     * 예약 상태 업데이트
     * @param bookingId 예약 ID
     * @param status 변경할 상태
     */
    void updateBookingStatus(Long bookingId, String status);

    /**
     * 만료된 예약 자동 취소 (스케줄러용)
     * - 5분 이상 경과한 PENDING 예약을 CANCELED로 변경
     * @return 취소된 예약 건수
     */
    int cancelExpiredBookings();
}

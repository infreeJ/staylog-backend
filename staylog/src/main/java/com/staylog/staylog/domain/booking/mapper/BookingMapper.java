package com.staylog.staylog.domain.booking.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 예약 Mapper 인터페이스
 */
@Mapper
public interface BookingMapper {

    /**
     * 객실 가용성 체크 (중복 예약 확인)
     * @param roomId 객실 ID
     * @param checkIn 체크인 날짜
     * @param checkOut 체크아웃 날짜
     * @return 중복 예약 건수 (0이면 예약 가능)
     */
    int checkRoomAvailability(@Param("roomId") Long roomId,
                              @Param("checkIn") LocalDate checkIn,
                              @Param("checkOut") LocalDate checkOut);

    /**
     * 예약 생성
     * @param params 예약 정보 (userId, roomId, bookingNum, amount, checkIn, checkOut, status, guestName, adults, children, infants)
     */
    void insertBooking(Map<String, Object> params);

    /**
     * 예약 조회 (bookingId)
     * @param bookingId 예약 ID
     * @return 예약 정보
     */
    Map<String, Object> findBookingById(@Param("bookingId") Long bookingId);

    /**
     * 예약 조회 (bookingNum)
     * @param bookingNum 주문번호
     * @return 예약 정보
     */
    Map<String, Object> findBookingByBookingNum(@Param("bookingNum") String bookingNum);

    /**
     * 예약 상태 업데이트
     * @param bookingId 예약 ID
     * @param status 변경할 상태 (RES_PENDING, RES_CONFIRMED, RES_CANCELED, etc.)
     */
    void updateBookingStatus(@Param("bookingId") Long bookingId,
                             @Param("status") String status);

    /**
     * 예약 만료 시간 업데이트 (결제 준비 시 결제 수단에 따라 연장)
     * @param bookingId 예약 ID
     * @param expiresAt 새로운 만료 시간
     */
    void updateExpiresAt(@Param("bookingId") Long bookingId,
                         @Param("expiresAt") LocalDateTime expiresAt);

    /**
     * 만료된 예약 조회 (스케줄러용)
     * @param now 현재 시각
     * @return 만료된 예약 목록
     */
    List<Map<String, Object>> findExpiredBookings(@Param("now") LocalDateTime now);
}

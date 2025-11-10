package com.staylog.staylog.domain.booking.mapper;

import com.staylog.staylog.domain.booking.dto.response.BookingDetailResponse;
import com.staylog.staylog.domain.booking.entity.Booking;
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
    void insertBooking(Booking params);

    /**
     * 예약 조회 (bookingId) - BookingDetailResponse 직접 반환
     * @param bookingId 예약 ID
     * @return 예약 상세 정보 (JOIN 포함)
     */
    BookingDetailResponse findBookingById(@Param("bookingId") Long bookingId);

    /**
     * 예약 조회 (bookingNum) - Booking Entity 반환
     * @param bookingNum 주문번호
     * @return 예약 정보 (Booking Entity)
     */
    Booking findBookingByBookingNum(@Param("bookingNum") String bookingNum);

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
     * 예약 최종 금액 업데이트 (결제 준비 시 쿠폰 적용 후)
     * @param bookingId 예약 ID
     * @param finalAmount 최종 결제 금액 (쿠폰 할인 적용 후)
     */
    void updateFinalAmount(@Param("bookingId") Long bookingId,
                           @Param("finalAmount") Long finalAmount);

    /**
     * 만료된 예약 조회 (스케줄러용)
     * @param now 현재 시각
     * @return 만료된 예약 목록 (일부 필드만 채워진 Booking Entity)
     */
    List<Booking> findExpiredBookings(@Param("now") LocalDateTime now);



    /**
     * bookingId로 숙소명 조회
     * @author 이준혁
     * @param bookingId 예약 PK
     * @return 숙소명
     */
    Map<String, Object> findAccommodationIdAndNameByBookingId(@Param("bookingId") long bookingId);

    /**
     * bookingId로 예약자 PK 조회
     * @author 이준혁
     * @param bookingId 예약 PK
     * @return 예약자 PK
     */
    long findUserIdByBookingId(@Param("bookingId") long bookingId);
}

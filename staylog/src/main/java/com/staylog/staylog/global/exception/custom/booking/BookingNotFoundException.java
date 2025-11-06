package com.staylog.staylog.global.exception.custom.booking;

import com.staylog.staylog.global.common.code.ErrorCode;

/**
 * 예약을 찾을 수 없을 때 발생하는 예외
 * HTTP 404 Not Found
 */
public class BookingNotFoundException extends BookingException {

    public BookingNotFoundException() {
        super(ErrorCode.BOOKING_NOT_FOUND);
    }

    public BookingNotFoundException(Long bookingId) {
        super(ErrorCode.BOOKING_NOT_FOUND,
              String.format("Booking not found: bookingId=%d", bookingId));
    }

    public BookingNotFoundException(String bookingNum) {
        super(ErrorCode.BOOKING_NOT_FOUND,
              String.format("Booking not found: bookingNum=%s", bookingNum));
    }
}

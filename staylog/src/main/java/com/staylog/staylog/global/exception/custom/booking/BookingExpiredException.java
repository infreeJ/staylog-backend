package com.staylog.staylog.global.exception.custom.booking;

import com.staylog.staylog.global.common.code.ErrorCode;

/**
 * 예약 만료 예외
 */
public class BookingExpiredException extends BookingException {
    public BookingExpiredException() {
        super(ErrorCode.BOOKING_EXPIRED);
    }

    public BookingExpiredException(Long bookingId) {
        super(ErrorCode.BOOKING_EXPIRED,
              String.format("Booking expired: bookingId=%d", bookingId));
    }
}

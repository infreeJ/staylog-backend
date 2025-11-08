package com.staylog.staylog.global.exception.custom.booking;

import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.constant.ReservationStatus;

/**
 * 예약 상태가 PENDING이 아닌 경우 예외
 */
public class BookingNotPendingException extends BookingException {
    public BookingNotPendingException() {
        super(ErrorCode.BOOKING_NOT_PENDING);
    }

    public BookingNotPendingException(String currentStatus) {
        super(ErrorCode.BOOKING_NOT_PENDING,
              String.format("Booking is not pending: current status=%s", currentStatus));
    }

    public BookingNotPendingException(ReservationStatus currentStatus) {
        super(
                ErrorCode.BOOKING_NOT_PENDING,
                String.format("Booking is not pending: current status=%s", currentStatus.getCode())
        );
    }
}

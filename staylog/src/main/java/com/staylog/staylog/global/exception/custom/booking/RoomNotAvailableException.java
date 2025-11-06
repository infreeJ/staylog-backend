package com.staylog.staylog.global.exception.custom.booking;

import com.staylog.staylog.global.common.code.ErrorCode;

/**
 * 객실 예약 불가 예외 (중복 예약 등)
 */
public class RoomNotAvailableException extends BookingException {
    public RoomNotAvailableException() {
        super(ErrorCode.ROOM_NOT_AVAILABLE);
    }

    public RoomNotAvailableException(Long roomId) {
        super(ErrorCode.ROOM_NOT_AVAILABLE,
              String.format("Room not available: roomId=%d", roomId));
    }
}

package com.staylog.staylog.domain.admin.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 관리자 예약목록 응답 객체
 */
@Data
public class AdminBookingDto {
    private Long bookingId;
    private Long userId;
    private String userName;
    private Long accommodationId;
    private String accommodationName;
    private Long roomId;
    private String roomName;
    private String status;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
}

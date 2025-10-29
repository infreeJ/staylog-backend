package com.staylog.staylog.domain.mypage.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingInfoResponse {
    private Long bookingId;
    private String accommodationName;
    private String roomName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String status;
    private LocalDateTime createdAt;
}


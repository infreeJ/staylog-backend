package com.staylog.staylog.domain.board.dto;

import lombok.Data;

@Data
public class BookingDto {

    private Long bookingId;
    private String regionCode;
    private String regionName;
    private Long accommodationId;
    private String accommodationName;
    private String checkIn;
    private String checkOut;
}

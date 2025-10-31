package com.staylog.staylog.domain.admin.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관리자 예약 목록 조회 요청 Dto
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminBookingListRequest {
    private String status; // 예약 상태
    private String guestName; // 투숙객
    private String userName; // 회원 이름

}

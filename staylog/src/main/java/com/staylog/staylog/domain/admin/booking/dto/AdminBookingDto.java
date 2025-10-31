package com.staylog.staylog.domain.admin.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 관리자 예약목록 응답 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminBookingDto {
    private Long bookingId;          // 예약 ID
    private String bookingNum;       // 예약 번호
    private String guestName;        // 투숙객 이름
    private String userName;         // 회원 이름 (JOIN 필요)
    private String accommodationName;// 숙소명 (JOIN 필요)
    private String roomName;         // 객실명 (JOIN 필요)
    private LocalDateTime checkIn;       // 체크인
    private LocalDateTime checkOut;      // 체크아웃
    private Integer adults;          // 성인 수
    private Integer children;        // 어린이 수
    private Integer infants;         // 유아 수
    private Integer totalGuestCount; // 총 인원수
    private Integer amount;          // 결제 금액
    private String status;           // 예약 상태 코드
    private String statusName;       // 예약 상태명 (COMMON_CODE 참조)
    private String statusColor;      // 상태 색상 (COMMON_CODE.ATTR1)
    private LocalDateTime createdAt; // 예약일 (결제일)

}

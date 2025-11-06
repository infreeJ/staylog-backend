package com.staylog.staylog.domain.mypage.dto.response;

// 날짜/시간 API, 타입 안정성이 높고, 불변 객체, DB에선 TIMESTAMP 타입 컬럼과 매핑된다.
import java.time.LocalDateTime; 

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 마이페이지 예약 정보 DTO (회원 본인의 모든 예약 정보 조회)
 * @Author 오미나
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingInfoResponse {
	// 예약 정보 
    private Long bookingId; 			// 예약 고유번호 내부 (RESERVATION.BOOKING_ID(PK))
    private String bookingNum; 			// 회원에게 보이는 예약번호 (RESERVATION.BOOKING_ID(PK))
    
    // 결제 및 투숙자 정보
    private Long payId;               	// 결제 ID (PAYMENT.PAY_ID)
    private String guestName;         	// 실 투숙자명
    private Integer amount;           	// 결제 금액
    private Integer totalGuestCount;  	// 총 인원 수
    
    // 숙소 & 객실 정보
    private String accommodationName; 	// 숙소명 (ACCOMMODATION.NAME)
    private String roomName; 			// 객실명 (ROOM.NAME)
    private Long roomId;				// 객실 ID (ROOM_ID)
    
    // 예약 일정 & 상태/생성/수정일 
    private LocalDateTime checkIn;  	// 체크인 날짜 
    private LocalDateTime checkOut; 	// 체크아웃 날짜
    private String status; 				// 예약 상태 (CONFIRMED, CANCELED, REFUNDED)
    private LocalDateTime createdAt; 	// 예약 생성일
    private LocalDateTime updatedAt; 	// 예약 수정일
    
    
}


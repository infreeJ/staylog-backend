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
    private String bookingNum; 			// 회원에게 보이는 예약번호 (RESERVATION.BOOKING_NUM)
    
    // 사용자 정보
    private String userName;			// 사용자(회원) 명
    private String phone;				// 전화번호 
    
    // 투숙객 정보
    private String guestName;         	// 실 투숙자명
    private Integer adults;           	// 성인
    private Integer children;           // 어린이
    private Integer infants;           	// 영유아
    private Integer totalGuestCount;  	// 총 인원 수
    
    // 숙소 & 객실 정보
    private Long accommodationId;		// 숙소 ID (ACCOMMODATION.ACCOMMODATION_ID)
    private String accommodationName; 	// 숙소명 (ACCOMMODATION.NAME)
    private Long roomId;				// 객실 ID (ROOM.ROOM_ID)
    private String roomName; 			// 객실명 (ROOM.NAME)
    
    // 결제 정보
    private Long payId;               	// 결제 ID (PAYMENT.PAY_ID)
    private Integer amount;           	// 결제 금액
    private String paymentMethod;		// 결제 방식
    private LocalDateTime paidAt;		// 결제일

    
    // 예약 일정 & 상태/생성/수정일 
    private String checkIn;  			// 체크인 날짜 
    private String checkOut; 			// 체크아웃 날짜
    private String status; 				// 예약 상태 (RESERVATION.RES_CONFIRMED, RES_CANCELED, RES_REFUNDED)
    private LocalDateTime createdAt; 	// 예약 생성일
    private LocalDateTime updatedAt; 	// 예약 수정일
    
    
}


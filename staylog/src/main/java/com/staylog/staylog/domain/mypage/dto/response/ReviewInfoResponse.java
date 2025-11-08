package com.staylog.staylog.domain.mypage.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewInfoResponse {
    private Long reviewId; 				// 리뷰 고유 ID
    private Long bookingId; 			// 예약 고유번호 내부 (RESERVATION.BOOKING_ID(PK))
    private String bookingNum; 			// 회원에게 보이는 예약번호 (RESERVATION.BOOKING_NUM)
    private String accommodationName; 	// 숙소명
    private String checkIn; 			// 체크인
    private String checkOut; 		  	// 체크아웃
    private String title; 				// 리뷰 제목
    private Integer rating; 			// 별점
    private LocalDateTime createdAt; 	// 작성일
}



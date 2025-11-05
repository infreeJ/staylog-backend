package com.staylog.staylog.domain.mypage.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewInfoResponse {
    private Long reviewId; //리뷰 고유 ID
    private Long bookingId; //예약번호
    private String accomodationName; //숙소명
    private String content; //리뷰 내용 
    private Integer rating; //별점
    private LocalDateTime createdAt; //작성일
}



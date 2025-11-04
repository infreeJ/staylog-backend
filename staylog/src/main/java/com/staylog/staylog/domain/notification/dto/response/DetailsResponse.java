package com.staylog.staylog.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsResponse {
    private String imageUrl;
    private String date; // 예약타입 -> 체크인~체크아웃 / 댓글,리뷰타입 -> 작성일
    private String title; // 예약타입 -> 숙소명 / 댓글,리뷰타입 -> 작성자
    private String message; // 예약타입 -> 예약문구 / 댓글,리뷰타입 -> 내용
    private String typeName; // Reservation / Comment / Review
}

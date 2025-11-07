package com.staylog.staylog.global.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 리뷰게시글 작성 이벤트 객체
 * @author 이준혁
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreatedEvent {
    private long boardId; // 작성된 게시글 PK
    private long accommodationId; // 게시글과 연결된 숙소 PK
    private long bookingId; // 게시글과 연결된 예약 PK
    private long userId; // 작성자 PK
}

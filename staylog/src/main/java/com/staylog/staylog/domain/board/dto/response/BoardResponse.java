package com.staylog.staylog.domain.board.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 게시글 상세 응답 DTO
 *
 * @author 김지은
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {

    // 기본 정보
    private long boardId;          // 리뷰 고유번호 (PK)
    private long accommodationId;  // 숙소 고유번호
    private long bookingId;        // 예약 고유번호 (UNIQUE)
    private long userId;           // 작성자 고유번호

    // 지역 및 게시판 카테고리
    private String regionCode; // 지역 코드 (common_code.code_id)
    private String boardType;  // 게시판 카테고리 (REVIEW, JOURNAL, ...)

    // 본문
    private String title;           // 리뷰 제목
    private String content;         // 리뷰 내용
    private Integer likesCount;    // 좋아요 수
    private Integer rating;         // 별점 (1~5)
    private Integer viewsCount;    // 조회 수

    // 날짜 및 삭제 여부
    private LocalDateTime createdAt;    // 작성일
    private LocalDateTime updatedAt;    // 수정일
    private String deleted;         // 삭제 여부 ('Y'/'N')

}

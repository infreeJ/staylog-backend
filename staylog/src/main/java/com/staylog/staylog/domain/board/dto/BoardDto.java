package com.staylog.staylog.domain.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 게시판 관련 Dto
 * 저널 / 리뷰 등 공통으로 사용
 * 게시판 종류는 target_type(enum)으로 관리
 *
 * @author 김지은
 */

@Alias("BoardDto")
@Getter
@Setter
public class BoardDto {

    private long board_id;          // 리뷰 고유번호 (PK)
    private long accommodation_id;           // 숙소 고유번호
    private long booking_id;        // 예약 고유번호 (UNIQUE)
    private long user_id;           // 작성자 고유번호

    private String region_type; // 지역 코드 (common_code.code_id)
    private String board_type;  // enum - 작성 게시판 카테고리 (0: REVIEW, 1: JOURNAL)

    private String title;           // 리뷰 제목
    private String content;         // 리뷰 내용
    private Integer likes_count;    // 좋아요 수
    private Integer rating;         // 별점 (1~5)
    private Integer views_count;    // 조회 수

    private LocalDateTime created_at;    // 작성일
    private LocalDateTime updated_at;    // 수정일
    private String deleted;         // 삭제 여부 ('Y'/'N')

}


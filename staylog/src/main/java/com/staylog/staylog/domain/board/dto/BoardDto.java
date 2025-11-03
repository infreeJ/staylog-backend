package com.staylog.staylog.domain.board.dto;

import lombok.Builder;
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
@Builder
public class BoardDto {

    private long boardId;          // 리뷰 고유번호 (PK)
    private long accommodationId;  // 숙소 고유번호
    private long bookingId;        // 예약 고유번호 (UNIQUE)
    private long userId;           // 작성자 고유번호

    private String regionCode;      // 지역 코드 (common_code.code_id)
    private String boardType;       //  카테고리 (REVIEW, JOURNAL)

    private String title;           // 리뷰 제목
    private String content;         // 리뷰 내용
    private Integer likesCount;     // 좋아요 수
    private Integer rating;         // 별점 (1~5)
    private Integer viewsCount;     // 조회 수

    private LocalDateTime createdAt;    // 작성일
    private LocalDateTime updatedAt;    // 수정일
    private String deleted;             // 삭제 여부 ('Y'/'N')

}


package com.staylog.staylog.domain.board.dto;


import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 댓글 관련 Dto
 * 저널 / 리뷰 등 공통으로 사용
 * 게시판 종류는 target_type(enum)으로 관리
 *
 * @author 김지은
 */

@Alias("CommentsDto")
@Getter
@Setter
public class CommentsDto {

    private Long comment_id;    // 댓글 고유번호 (PK)
    private String board_type; // 댓글 카테고리 (REVIEW, JOURNAL)
    private Long board_id;     // 댓글 대상 ID (journal_id 또는 review_id)
    private Long user_id;       // 작성자 고유번호 (FK)
    private Long parent_id;     // 대댓글용 (부모 댓글 ID)

    private String content;     // 댓글 내용
    private LocalDateTime created_at;  // 작성일
    private LocalDateTime updated_at;  // 수정일
    private String deleted;     // 삭제 여부 ('Y'/'N')
}

package com.staylog.staylog.domain.board.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 댓글 관련 Dto
 * 저널 / 리뷰 등 공통으로 사용
 * 게시판 종류는 공통 코드 (BOARD_TYPE => BOARD_REVIEW, BOARD_JOURNAL 등) 으로 관리
 *
 * @author 김채린
 */

@Alias("CommentsDto")
@Data
public class CommentsDto {

    private Long commentId; // 댓글 고유번호 (PK)
    private String boardType; // 댓글 카테고리 (BOARD_REVIEW, BOARD_JOURNAL)
    private Long boardId; // 댓글 대상 ID (journal_id 또는 review_id)
    private Long userId; // 작성자 고유번호
    private Long parentId; // 대댓글용 (부모 댓글 ID)

    private String content; // 댓글 내용
    private LocalDateTime createdAt; // 작성일
    private LocalDateTime updatedAt; // 수정일
    private String deleted; // 삭제 여부 ('Y'/'N')
    
    private String nickname; // 회원 닉네임
    private String profileUrl; // 회원 프로필
}

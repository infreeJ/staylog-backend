package com.staylog.staylog.domain.board.dto;



import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Alias("LikesDto")
@Getter
@Setter
public class LikesDto {

    private Long likeId;       // 좋아요 고유번호 (PK)
    private String boardType; // 카테고리 (REVIEW / JOURNAL)
    private Long userId;       // 사용자 고유번호 (FK)
    private Long boardId;     // 좋아요 대상 ID (review_id 또는 journal_id)
}

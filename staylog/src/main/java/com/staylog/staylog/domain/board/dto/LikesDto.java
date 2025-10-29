package com.staylog.staylog.domain.board.dto;



import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Alias("LikesDto")
@Getter
@Setter
public class LikesDto {

    private Long like_id;       // 좋아요 고유번호 (PK)
    private String board_type; // 카테고리 (REVIEW / JOURNAL)
    private Long user_id;       // 사용자 고유번호 (FK)
    private Long board_id;     // 좋아요 대상 ID (review_id 또는 journal_id)
}

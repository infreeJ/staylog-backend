package com.staylog.staylog.domain.admin.board.dto;

import org.apache.ibatis.type.Alias;

import com.staylog.staylog.global.common.dto.PageRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("BoardSearchRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminBoardSearchRequest extends PageRequest {
	
	/** 게시글 타입 (필수) */
    private String boardType;      // BOARD_REVIEW, BOARD_JOURNAL
    
    /** 검색 조건 */
    private String keyword;        // 검색어
    private String searchType;     // accommodation, nickname
    private String deleted;      // Y, N
    
    /** 정렬조건 */
    private String sortBy;         // createdAt, viewsCount, rating, likes
    private String sortOrder;      // ASC, DESC (기본값: DESC)
    
    /** 정렬조건 기본값 설정 */
    public String getSortBy() {
        return sortBy != null ? sortBy : "createdAt";
    }
    
    public String getSortOrder() {
        return sortOrder != null ? sortOrder : "DESC";
    }
}
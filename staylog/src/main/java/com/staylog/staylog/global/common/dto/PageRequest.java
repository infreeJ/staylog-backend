package com.staylog.staylog.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 페이지 요청 정보 DTO
 *
 * - React 에서 요청 시 전달되는 파라미터
 * - 페이지 번호, 페이지 크기, 검색 관련 조건 포함
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {

    private int pageNum = 1;   // 요청한 페이지 번호 (기본값 1)
    private int pageSize = 10;  // 한 페이지당 게시글 수

    private String boardType;   // 게시판 카테고리 (Review, Journal..)
    private String keyword;     // 검색어
    private String search;      // 검색 조건 ('title', 'content'...)

    // DB 쿼리용 offset
    public int getOffset(){
        return (pageNum -1)*pageSize;
    }
}

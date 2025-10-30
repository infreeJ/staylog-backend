package com.staylog.staylog.domain.board.dto.response;

import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.global.common.response.PageResponse;

import lombok.*;

import java.util.List;

/**
 * 게시판 목록 응답 DTO
 *
 * - 게시판 리스트
 * - 페이징 정보
 * - 검색/조건
 *
 * - React, Swagger 등에서 JSON으로 바로 반환되는 구조
 *
 * @author 김지은
 */

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardListResponse {

    private List<BoardDto> boardList;   // 게시글 목록

    // Pagination
    private PageResponse pageResponse;      // 페이징 처리 - Pagination 클래스 재사용

    private String boardType;   // 게시판 카테고리 (review, journal, 공지사항..)
    private String keyword;     // 검색어
    private String search;      // 검색 조건 구분 ('title', 'content', ...)
    private String query;       // 검색 쿼리 문자열 ( keyword + search )


}

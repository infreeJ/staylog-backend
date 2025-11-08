package com.staylog.staylog.global.common.dto;

import lombok.*;

/**
 * 페이지 요청 정보 DTO
 *
 * - React 에서 요청 시 전달되는 파라미터
 * - 페이지 번호, 페이지 크기, 검색 관련 조건 포함
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {

    private int pageNum;   // 요청한 페이지 번호 (기본값 1)
    private int pageSize;  // 한 페이지당 게시글 수

    // ROWNUM에서 사용할 시작 행 번호 (예: 1, 11, 21...)
    public int getStartRow() {

        return (pageNum - 1) * pageSize + 1; 
    }
    
    // ROWNUM에서 사용할 끝 행 번호 (예: 10, 20, 30...)
    public int getEndRow() {

        return pageNum * pageSize;
    }
}

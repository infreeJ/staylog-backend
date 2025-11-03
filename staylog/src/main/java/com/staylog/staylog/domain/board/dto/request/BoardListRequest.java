package com.staylog.staylog.domain.board.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListRequest {

    private String boardType;   // 게시판 카테고리
    private String keyword;     // 검색어
    private String search;      // 검색 조건 (title, content,,)
    private String regionCode;  // 지역 코드
    private String regionName;  // 지역 이름

    // 페이징용 추가 필드 (Mapper 바인딩용)
    private int offset;
    private int pageSize;

}

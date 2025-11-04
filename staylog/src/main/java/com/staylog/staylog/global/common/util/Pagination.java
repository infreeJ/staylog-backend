package com.staylog.staylog.global.common.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 페이징 처리 - dto/response 에서 
 * 
 * 이거는 미사용
 *
 * @Author 김지은
 *
 * 글 10개 / 1page
 * 5page / 화면
 *
 * < 1 2 3 4 5 >
 *
 */


@NoArgsConstructor
@AllArgsConstructor
public class Pagination {

    private int currentPage;    // 현재 페이지 번호
    private int pageSize;       // 페이지당 게시글 수
    private int totalCount;     // 전체 게시글 수
    private int totalPage;      // 전체 페이지 수
    private int startPage;      // 그룹의 시작 페이지 (1, 11, 21...)
    private int endPage;        // 그룹의 끝 페이지 (10, 20, 30...)
    private int offset;         // DB 쿼리용 offset 계산용 필드

    // 페이지 계산 로직
    public void calculate(){
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);   // 전체 페이지 수
        this.offset = (currentPage-1)*pageSize;     // offset

        // 한 화면에 10개 페이지 단위로 보여주기
        int pageGroupSize = 5;
        this.startPage = ((currentPage-1)/pageGroupSize)*pageGroupSize+1;
        this.endPage = Math.min(totalPage, startPage + pageGroupSize -1);
    }

}

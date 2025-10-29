package com.staylog.staylog.global.common.response;

import com.staylog.staylog.global.common.dto.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 페이지 응답 DTO
 *
 * - 게시판, 댓글 등 여러 도메인에서 재사용 가능
 *
 * - DB 조회 결과를 기반으로 계산된 페이징 데이터
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {

    private int pageNum; // 페이지 번호
    private int pageSize;   // 페이지당 게시글 수
    private int totalCount; // 전체 게시글 수
    private int totalPage;  // 전체 페이지 수
    private int startPage;  // 현재 그룹의 시작 페이지
    private int endPage;    // 현재 그룹의 끝 페이지
    private int offset;     // DB OFFSET (LIMIT 시작점)


    /**
     * 페이지 계산 메서드
     * - pageRest / totalCount 기반으로 계산
     */
    public void calculate(PageRequest request, int totalCount) {
        this.pageNum = request.getPageNum();
        this.pageSize = request.getPageSize();
        this.totalCount = totalCount;

        // 전체 페이지 수 계산
        this.totalPage = (int)Math.ceil((double)this.totalCount / pageSize);

        // offset 계산
        this.offset = (this.pageNum - 1) * pageSize;

        // 한 화면에 표시할 페이지 개수
        int pageGroupSize = 5;
        this.startPage = (this.pageNum - 1) / pageGroupSize * pageGroupSize + 1;
        this.endPage = Math.min(this.totalPage, startPage + pageGroupSize - 1);


    }
}

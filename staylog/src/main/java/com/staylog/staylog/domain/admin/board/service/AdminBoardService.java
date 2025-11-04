package com.staylog.staylog.domain.admin.board.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.staylog.staylog.domain.admin.board.dto.AdminBoardSearchRequest;
import com.staylog.staylog.domain.admin.board.dto.AdminBoardStatusRequest;
import com.staylog.staylog.domain.board.dto.BoardDto;

/**
 * 관리자 게시글 관리 서비스 인터페이스
 * 게시글의 삭제/복원, 조회 기능을 제공합니다.
 *
 * @author 천승현
 */
public interface AdminBoardService {
	
    /**
     * 게시글 목록 조회
     * @param BoardSearchDto 검색 조건 (게시글타입, 키워드, 삭제여부, 정렬조건)
     * @return 게시글 목록
     */
    List<BoardDto> selectBoardList(AdminBoardSearchRequest searchRequest);
	
    /**
     * 게시글 상세 조회
     * @param boardId 게시글 ID
     * @return 게시글 상세 정보
     */
    BoardDto selectBoardDetail(@Param("boardId") Long boardId);

    /**
     * 게시글 상태전환 (삭제/복원)
     * @param request 게시글 상태 변경 요청 DTO (boardId, deletedYn 포함)
     * @return 삭제된 행 수
     */
    void updateBoardStatus(AdminBoardStatusRequest request);
}
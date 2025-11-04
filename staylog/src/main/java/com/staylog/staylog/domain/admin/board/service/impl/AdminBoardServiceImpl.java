package com.staylog.staylog.domain.admin.board.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.admin.board.dto.AdminBoardSearchRequest;
import com.staylog.staylog.domain.admin.board.dto.AdminBoardStatusRequest;
import com.staylog.staylog.domain.admin.board.mapper.AdminBoardMapper;
import com.staylog.staylog.domain.admin.board.service.AdminBoardService;
import com.staylog.staylog.domain.board.dto.BoardDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 게시글 관리 서비스 구현체
 * 게시글의 삭제/복원, 조회 기능을 제공합니다.
 *
 * @author 천승현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminBoardServiceImpl implements AdminBoardService{
	
	private final AdminBoardMapper mapper;
	
    /**
     * 게시글 목록 조회
     * @param BoardSearchDto 검색 조건 (게시글타입, 키워드, 삭제여부, 정렬조건)
     * @return 게시글 목록
     */
	@Override
	public List<BoardDto> selectBoardList(AdminBoardSearchRequest searchRequest) {
		
		return mapper.selectBoardList(searchRequest);
	}
	
	
    /**
     * 게시글 상세 조회
     * @param boardId 게시글 ID
     * @return 게시글 상세 정보
     */
	@Override
	public BoardDto selectBoardDetail(Long boardId) {
		
		return mapper.selectBoardDetail(boardId);
	}
	
    /**
     * 게시글 상태전환 (삭제/복원)
     * @param request 게시글 상태 변경 요청 DTO (boardId, deletedYn 포함)
     * @return 삭제된 행 수
     */
	@Override
	public void updateBoardStatus(AdminBoardStatusRequest request) {
		
		mapper.updateBoardStatus(request);
	}
}

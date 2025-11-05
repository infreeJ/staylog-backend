package com.staylog.staylog.domain.admin.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.admin.board.dto.AdminBoardSearchRequest;
import com.staylog.staylog.domain.admin.board.dto.AdminBoardStatusRequest;
import com.staylog.staylog.domain.admin.board.service.AdminBoardService;
import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 게시글 관리 서비스 구현체
 * 게시글의 삭제/복원, 조회 기능을 제공합니다.
 *
 * @author 천승현
 */
@Slf4j
@Tag(name = "AdminAccommodationController", description = "관리자 숙소 관리 API")
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class AdminBoardController {
	 private final AdminBoardService boardService;
	 private final MessageUtil messageUtil;
	 
	    /**
	     * 게시글 목록 조회 (검색 필터 포함)
	     * 
	     * @param BoardSearchDto 검색 조건 (게시글타입, 키워드, 삭제여부, 정렬조건)
	     * @return 게시글 목록
	     */
	    @Operation(
	        summary = "게시글 목록 조회", 
	        description = "검색 조건에 맞는 게시글 목록을 조회합니다. 게시글타입을 제외한 모든 파라미터는 선택사항입니다."
	    )
	    @GetMapping("/admin/board")
	    public ResponseEntity<SuccessResponse<List<BoardDto>>> getlist(
	            @Parameter(description = "게시글 검색 조건") AdminBoardSearchRequest searchRequest) {
	    	List<BoardDto> list = boardService.selectBoardList(searchRequest);
	        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
	        String code = SuccessCode.SUCCESS.name();
	        SuccessResponse<List<BoardDto>> success = SuccessResponse.of(code, message, list);
	        return ResponseEntity.ok(success);
	    }
	    
	    /**
	     * 게시글 상세 조회
	     * 
	     * @param boardId 게시글 ID
	     * @return 게시글 상세 정보
	     */
	    @Operation(
	        summary = "게시글 상세 조회", 
	        description = "특정 게시글의 상세 정보를 조회합니다."
	    )
	    @GetMapping("/admin/board/{boardId}")
	    public ResponseEntity<SuccessResponse<BoardDto>> getboard(
	            @Parameter(description = "게시글 ID") 
	            @PathVariable Long boardId) {
	    	BoardDto dto = boardService.selectBoardDetail(boardId);
	        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
	        String code = SuccessCode.SUCCESS.name();
	        SuccessResponse<BoardDto> success = SuccessResponse.of(code, message, dto);
	        return ResponseEntity.ok(success);
	    }

	    /**
	     * 게시글 상태전환 (삭제/복원)
	     * 요청 DTO의 deletedYn 값에 따라 숙소의 상태를 'Y' (삭제) 또는 'N' (복원)으로 변경합니다.
	     * 
	     * @param boardId 상태를 변경할 게시글 ID (URL 경로에서 추출)
	     * @param request 변경할 상태 값 (deletedYn = 'Y' 또는 'N')
	     */
	    @Operation(
			summary = "게시글 상태 전환 (삭제/복원)", 
	        description = "게시글의 논리적 삭제 여부(deleted_yn)를 'Y' 또는 'N'으로 변경합니다."
	    )
	    @PatchMapping("/admin/board/{boardId}/status")
	    public ResponseEntity<SuccessResponse<Void>> updateBoardStatus(
	    		@Parameter(description = "상태를 변경할 게시글 ID") 
	            @PathVariable Long boardId, // @PathVariable은 ID를 받도록 수정
	            @RequestBody AdminBoardStatusRequest request) {
	    	request.setBoardId(boardId);
	    	boardService.updateBoardStatus(request);
	        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
	        String code = SuccessCode.SUCCESS.name();
	        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
	    }
}

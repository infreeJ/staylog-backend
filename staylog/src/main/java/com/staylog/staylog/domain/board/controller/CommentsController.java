package com.staylog.staylog.domain.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.staylog.staylog.domain.board.dto.CommentsDto;
import com.staylog.staylog.domain.board.service.CommentsService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 댓글 관련 컨트롤러
 * GET : 조회
 * POST : 등록, 수정, 삭제
 * 
 * @author 김지은
 */
@Tag(name = "CommentsController", description = "댓글 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CommentsController {

	private final CommentsService commentsService;
	private final MessageUtil messageUtil;

	// 특정 게시글 댓글 목록 조회
	@Operation(summary = "댓글 목록 조회", description = "게시글 ID로 댓글 목록을 조회")
	@GetMapping("/comments/{boardId}")
	public ResponseEntity<SuccessResponse<List<CommentsDto>>> getCommentsByBoardId(
	        @PathVariable("boardId") Long boardId) {
	    
	    log.info("댓글 목록 조회 요청 : boardId = {}", boardId);

	    List<CommentsDto> data = commentsService.getByBoardId(boardId);
	    String msg = messageUtil.getMessage(SuccessCode.COMMENTS_LIST_FETCHED.getMessageKey());
	    String code = SuccessCode.COMMENTS_LIST_FETCHED.name();

	    return ResponseEntity.ok(SuccessResponse.of(code, msg, data));
	}

	/**
	 * 댓글 등록
	 * @param dto 등록할 댓글 정보
	 * @return 등록 완료 응답
	 */
	@Operation(summary = "댓글 등록", description = "새로운 댓글을 등록")
	@PostMapping("/comments")
	public ResponseEntity<SuccessResponse<Void>> insertComment(@RequestBody CommentsDto dto) {
		log.info("댓글 등록 요청 : dto = {}", dto);
		
		commentsService.insert(dto);
		String msg = messageUtil.getMessage(SuccessCode.COMMENTS_CREATED.getMessageKey());
		String code = SuccessCode.COMMENTS_CREATED.name();
		
		return ResponseEntity.ok(SuccessResponse.of(code, msg, null));
	}

	/**
	 * 댓글 수정
	 * @param commentId 수정할 댓글 ID
	 * @param dto 수정 내용
	 * @return 수정 완료 응답
	 */
	@Operation(summary = "댓글 수정", description = "해당 댓글 내용을 수정")
	@PostMapping("/comments/update/{commentId}")
	public ResponseEntity<SuccessResponse<Void>> updateComment(@PathVariable Long commentId,
	                                                           @RequestBody CommentsDto dto) {
		log.info("댓글 수정 요청 : commentId = {}, dto = {}", commentId, dto);
		
		dto.setCommentId(commentId);
		commentsService.update(dto);
		
		String msg = messageUtil.getMessage(SuccessCode.COMMENTS_UPDATED.getMessageKey());
		String code = SuccessCode.COMMENTS_UPDATED.name();
		return ResponseEntity.ok(SuccessResponse.of(code, msg, null));
	}

	/**
	 * 댓글 삭제 (논리 삭제)
	 * @param commentId 삭제할 댓글 ID
	 * @return 삭제 완료 응답
	 */
	@Operation(summary = "댓글 삭제", description = "해당 댓글을 삭제")
	@PostMapping("/comments/delete/{commentId}")
	public ResponseEntity<SuccessResponse<Void>> deleteComment(@PathVariable Long commentId) {
		log.info("댓글 삭제 요청 : commentId = {}", commentId);
		
		CommentsDto dto = new CommentsDto();
		dto.setCommentId(commentId);
		commentsService.delete(dto);
		
		String msg = messageUtil.getMessage(SuccessCode.COMMENTS_DELETED.getMessageKey());
		String code = SuccessCode.COMMENTS_DELETED.name();
		return ResponseEntity.ok(SuccessResponse.of(code, msg, null));
	}
}
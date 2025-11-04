package com.staylog.staylog.domain.board.controller;

import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.domain.board.dto.BookingDto;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;

import com.staylog.staylog.domain.board.service.BoardService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.dto.PageRequest;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시판 컨트롤러
 * @author 김지은
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardController {

    private final BoardService boardService;
    private final MessageUtil messageUtil;

    // 게시판 카테고리별 목록 조회
    @GetMapping("/boards")
    public ResponseEntity<SuccessResponse<BoardListResponse>> boardList(@ModelAttribute BoardDto boardDto,
                                                                        @ModelAttribute PageRequest pageRequest) {

        BoardListResponse response = boardService.getByBoardType(boardDto, pageRequest);
        String message = messageUtil.getMessage(SuccessCode.BOARD_LIST_FETCHED.getMessageKey());
        String code = SuccessCode.BOARD_LIST_FETCHED.name();

        SuccessResponse<BoardListResponse> success = SuccessResponse.of(code, message, response);

        return ResponseEntity.ok(success);

    }

    // 게시판 등록
    @PostMapping("/boards")
    public ResponseEntity<SuccessResponse<Void>> boardCreate(@RequestBody BoardDto boardDto) {

        boardService.insert(boardDto);

        String code = SuccessCode.BOARD_CREATED.name();
        String message = messageUtil.getMessage(SuccessCode.BOARD_CREATED.getMessageKey());

        SuccessResponse<Void> success = SuccessResponse.of(code, message, null);

        return ResponseEntity.ok(success);

    }


    // 게시판 수정
    @PatchMapping("/boards")
    public ResponseEntity<SuccessResponse<BoardDto>> boardUpdate(@ModelAttribute BoardDto boardDto) {

        boardService.update(boardDto);

        String code = SuccessCode.BOARD_UPDATED.name();
        String message = messageUtil.getMessage(SuccessCode.BOARD_UPDATED.getMessageKey());

        SuccessResponse<BoardDto> success = SuccessResponse.of(code, message, boardDto);

        return ResponseEntity.ok(success);
    }

    // 게시판 삭제
    @DeleteMapping("/boards")
    public ResponseEntity<SuccessResponse<Void>> boardDelete(long boardId) {

        boardService.delete(boardId);

        String code = SuccessCode.BOARD_DELETED.name();
        String message = messageUtil.getMessage(SuccessCode.BOARD_DELETED.getMessageKey());

        SuccessResponse<Void> success = SuccessResponse.of(code, message, null);

        return ResponseEntity.ok(success);
    }

    // 예약내역 불러오기
    @GetMapping("/boards/bookings/{userId}")
    public ResponseEntity<SuccessResponse<List<BookingDto>>> bookingList(@PathVariable Long userId) {

        List<BookingDto> bookings = boardService.bookingList(userId);

        String code = SuccessCode.BOARD_LIST_FETCHED.name();
        String message = messageUtil.getMessage(SuccessCode.BOARD_LIST_FETCHED.getMessageKey());

        SuccessResponse<List<BookingDto>> success = SuccessResponse.of(code, message, bookings);
        return ResponseEntity.ok(success);

    }

    // 게시글 상세정보 불러오기
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<SuccessResponse<BoardDto>> getByBoardId(@PathVariable Long boardId) {

        BoardDto board = boardService.getByBoardId(boardId);

        String code = SuccessCode.BOARD_DETAIL_FETCHED.name();
        String message = messageUtil.getMessage(SuccessCode.BOARD_DETAIL_FETCHED.getMessageKey());

        SuccessResponse<BoardDto> success = SuccessResponse.of(code, message, board);
        return ResponseEntity.ok(success);
    }


}
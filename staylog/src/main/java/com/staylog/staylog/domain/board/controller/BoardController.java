package com.staylog.staylog.domain.board.controller;

import com.staylog.staylog.domain.board.dto.request.BoardListRequest;
import com.staylog.staylog.domain.board.dto.request.BoardRequest;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;
import com.staylog.staylog.domain.board.dto.response.BoardResponse;
import com.staylog.staylog.domain.board.service.BoardService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.dto.PageRequest;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 게시판 컨트롤러
 * @Author 김지은
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardController {

    private final BoardService boardService;
    private final MessageUtil messageUtil;

    // 게시판 카테고리별 목록 조회
    @GetMapping("/boards")
    public ResponseEntity<SuccessResponse<BoardListResponse>> boardList(@ModelAttribute BoardListRequest request,
                                                                  @ModelAttribute PageRequest pageRequest){

        BoardListResponse boardListResponse = boardService.getByBoardType(request,pageRequest);
        String message = messageUtil.getMessage(SuccessCode.BOARD_LIST_FETCHED.getMessageKey());
        String code = SuccessCode.BOARD_LIST_FETCHED.name();

        SuccessResponse<BoardListResponse> success = SuccessResponse.of(code, message, boardListResponse);

        return ResponseEntity.ok(success);

    }

    // 게시판 등록
    @PostMapping("/boards")
    public ResponseEntity<SuccessResponse<BoardResponse>> boardCreate (@ModelAttribute BoardRequest boardRequest){

        String code = SuccessCode.BOARD_CREATED.name();
        String message = messageUtil.getMessage(SuccessCode.BOARD_CREATED.getMessageKey());

        SuccessResponse<BoardResponse> success = SuccessResponse.of(code, message, null);
        return ResponseEntity.ok(success);


    }

//
//    // 게시판 수정
//    @PatchMapping("/boards")
//    public ResponseEntity<SuccessResponse<Void>> boardUpdate (@ModelAttribute BoardRequest request){
//
//
//    }
//
//    // 게시글 삭제
//    @DeleteMapping("/boards")
//    public ResponseEntity<SuccessResponse<Void>> boardDelete (@ModelAttribute BoardRequest request){
//
//
//    }

}

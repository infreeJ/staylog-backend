package com.staylog.staylog.domain.board.controller;

import com.staylog.staylog.domain.board.service.LikesService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.staylog.staylog.global.common.util.MessageUtil;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final MessageUtil messageUtil;

    // 게시글별 좋아요 수
    @GetMapping("likes/{boardId}")
    public ResponseEntity<SuccessResponse<Integer>> countByBoardId(@PathVariable long boardId) {

        int count = likesService.countByBoardId(boardId);

        String code = SuccessCode.BOARD_DETAIL_FETCHED.name();
        String message = messageUtil.getMessage(SuccessCode.BOARD_DETAIL_FETCHED.name());

        SuccessResponse<Integer> success = SuccessResponse.of(code, message, count);

        return ResponseEntity.ok(success);
    }

    // 사용자의 좋아요 여부
    @GetMapping("/likes/{boardId}/{userId}")
    public ResponseEntity<SuccessResponse<Integer>> liked(@PathVariable long boardId, long userId) {

        int liked =  likesService.liked(boardId, userId);

        String code = SuccessCode.BOARD_DETAIL_FETCHED.name();
        String message = SuccessCode.BOARD_DETAIL_FETCHED.name();
        SuccessResponse<Integer> success = SuccessResponse.of(code, message, liked);

        return ResponseEntity.ok(success);
    }





}

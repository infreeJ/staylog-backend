package com.staylog.staylog.domain.board.controller;

import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;
import com.staylog.staylog.domain.board.service.BoardService;
import com.staylog.staylog.global.common.dto.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardController {

    private final BoardService service;

    // 게시판 카테고리별 목록 조회
    @GetMapping("/board/list")
    public BoardListResponse list(@RequestParam String boardType,
                                  @RequestParam PageRequest request){


        request.setBoardType(boardType);


        return service.getByBoardType(boardType, request);
    }


}

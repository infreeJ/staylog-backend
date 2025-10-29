package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;
import com.staylog.staylog.global.common.dto.PageRequest;



public interface BoardService {

    BoardListResponse getByBoardType(String boardType, PageRequest request);
    void insert(BoardDto boardDto);
    void update(BoardDto boardDto);
    void delete(BoardDto boardDto);

}

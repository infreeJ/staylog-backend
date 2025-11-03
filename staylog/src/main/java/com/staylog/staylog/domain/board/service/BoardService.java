package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.domain.board.dto.request.BoardListRequest;
import com.staylog.staylog.domain.board.dto.request.BoardRequest;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;
import com.staylog.staylog.global.common.dto.PageRequest;



public interface BoardService {

    BoardListResponse getByBoardType(BoardListRequest boardListRequest, PageRequest pageRequest);
    
    void insert(BoardRequest boardRequest);
    void update(BoardRequest boardRequest);
    void delete(long boardId);

}

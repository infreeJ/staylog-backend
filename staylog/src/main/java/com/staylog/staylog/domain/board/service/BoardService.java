package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.domain.board.dto.BookingDto;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;
import com.staylog.staylog.global.common.dto.PageRequest;

import java.util.List;


public interface BoardService {

    BoardListResponse getByBoardType(BoardDto boardDto, PageRequest pageRequest);
    BoardDto getByBoardId(long boardId);

    void insert(BoardDto boardDto);
    void update(BoardDto boardDto);
    void delete(long boardId);

    List<BookingDto> bookingList(long userId);



}

package com.staylog.staylog.domain.board.mapper;

import com.staylog.staylog.domain.board.dto.BoardDto;

import java.util.List;

public interface BoardMapper {

    List<BoardDto> getByBoardType(String boardType);
    void insert(BoardDto boardDto);
    void update(BoardDto boardDto);
    void delete(BoardDto boardDto);



}

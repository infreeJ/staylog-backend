package com.staylog.staylog.domain.board.mapper;

import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.domain.board.dto.request.BoardListRequest;
import com.staylog.staylog.domain.board.dto.request.BoardRequest;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    BoardListResponse getByBoardType(BoardListRequest boardListRequest);
    void insert(BoardDto boardDto);
    void update(BoardDto boardDto);
    void delete(long boardId);

    int countByBoardType(String boardType);


}









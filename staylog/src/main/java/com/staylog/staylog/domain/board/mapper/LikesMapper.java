package com.staylog.staylog.domain.board.mapper;

import com.staylog.staylog.domain.board.dto.LikesDto;

import java.util.List;

public interface LikesMapper {

    int countByBoardId(int boardId);
    List<LikesDto> getByBoardId(int boardId);
    void insert(LikesDto dto);
    void delete(LikesDto dto);


}

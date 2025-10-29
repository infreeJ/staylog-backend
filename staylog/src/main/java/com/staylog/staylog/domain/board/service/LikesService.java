package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.LikesDto;

import java.util.List;

public interface LikesService {

    public List<LikesDto> getByBoardId(int boardId);
    public void insert(LikesDto dto);
    public void delete(LikesDto dto);
    public int countByBoardId(int boardId);

}

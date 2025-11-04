package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.LikesDto;

import java.util.List;

public interface LikesService {


    public void insert(LikesDto dto);
    public void delete(LikesDto dto);

    public int countByBoardId(long boardId);
    public int liked(long boardId, long userId);

    public List<LikesDto> getByBoardId(long boardId);
}

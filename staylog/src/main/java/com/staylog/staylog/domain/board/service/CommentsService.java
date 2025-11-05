package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.CommentsDto;

import java.util.List;

public interface CommentsService {

    public List<CommentsDto> getByBoardId(Long boardId);
    public void insert(CommentsDto dto);
    public void delete(CommentsDto dto);
    public void update(CommentsDto dto);

}

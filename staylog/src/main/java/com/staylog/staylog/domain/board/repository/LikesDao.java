package com.staylog.staylog.domain.board.repository;

import com.staylog.staylog.domain.board.dto.LikesDto;

import java.util.List;

public interface LikesDao {

    public int count(int boardId); // 좋아요 수
    public List<LikesDto> getByBoardId(int boardId);    // 좋아요한 user_id
    public void insert(LikesDto dto);   // 좋아요 등록
    public void delete(LikesDto dto);   // 좋아요 취소

}

package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.LikesDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface LikesService {


    public void toggleLike(LikesDto likesDto);

//    public void addLike(LikesDto dto);
//    public void deleteLike(LikesDto dto);

    public int countByBoardId(long boardId);
    public boolean liked(long boardId, long userId);

    public List<LikesDto> getByBoardId(long boardId);
}

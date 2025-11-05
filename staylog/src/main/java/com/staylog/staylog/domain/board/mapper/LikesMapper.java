package com.staylog.staylog.domain.board.mapper;

import com.staylog.staylog.domain.board.dto.LikesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikesMapper {

    int countByBoardId(long boardId);
    List<LikesDto> getByBoardId(long boardId);
    void addLike(LikesDto likesDto);
    void deleteLike(LikesDto likesDto);

    boolean liked(long boardId, long userId);


}

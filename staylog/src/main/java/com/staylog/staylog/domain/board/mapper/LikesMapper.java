package com.staylog.staylog.domain.board.mapper;

import com.staylog.staylog.domain.board.dto.LikesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikesMapper {

    int countByBoardId(int boardId);
    List<LikesDto> getByBoardId(int boardId);
    void insert(LikesDto dto);
    void delete(LikesDto dto);


}

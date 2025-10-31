package com.staylog.staylog.domain.board.mapper;

import com.staylog.staylog.domain.board.dto.CommentsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentsMapper {

    List<CommentsDto> getByBoardId(int boardId);
    void insert(CommentsDto dto);
    void delete(CommentsDto dto);
    void update(CommentsDto dto);



}

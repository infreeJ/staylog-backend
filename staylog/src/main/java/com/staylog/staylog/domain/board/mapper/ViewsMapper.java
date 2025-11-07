package com.staylog.staylog.domain.board.mapper;

import com.staylog.staylog.domain.board.dto.ViewDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ViewsMapper {

    public void addView(String viewToken, Long boardId);
    public int existsRecentView(String viewToken, Long boardId);
    public int countByBoardId(Long boardId);
}

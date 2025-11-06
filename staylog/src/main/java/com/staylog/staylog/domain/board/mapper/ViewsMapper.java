package com.staylog.staylog.domain.board.mapper;

import com.staylog.staylog.domain.board.dto.ViewDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ViewsMapper {

    public void addView(Long boardId, Long userId);
    public int existsRecentView(Long boardId, Long userId);
    public int countByBoardId(Long boardId);
}

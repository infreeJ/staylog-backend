package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.ViewDto;
import com.staylog.staylog.domain.board.mapper.BoardMapper;
import com.staylog.staylog.domain.board.mapper.ViewsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.View;

@Service
@RequiredArgsConstructor
public class ViewsServiceImpl implements ViewsService {

    private final ViewsMapper viewsMapper;
    private final BoardMapper boardMapper;

    @Override
    public int CountByBoardId(Long boardId) {
        return viewsMapper.countByBoardId(boardId);
    }

    @Override
    @Transactional
    public void addView(Long boardId, Long userId) {
        if (viewsMapper.existsRecentView(boardId, userId)==0) {
            viewsMapper.addView(boardId, userId);
            boardMapper.increaseViewsCount(boardId);
        }
    }


}

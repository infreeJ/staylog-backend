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
    public void addView(String viewToken, Long boardId) {
        // 최근 1시간 내 조회기록 확인
        int alreadyViewed = viewsMapper.existsRecentView(viewToken, boardId);

        if (alreadyViewed == 0) {
            // views 테이블에 insert
            viewsMapper.addView(viewToken, boardId);

            // board 테이블에 조회수 증가
            boardMapper.increaseViewsCount(boardId);
        }
    }


}

package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;
import com.staylog.staylog.global.common.dto.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {



    @Override
    public BoardListResponse getByBoardType(String boardType ,PageRequest request) {

        return null;
    }

    @Override
    public void insert(BoardDto boardDto) {

    }

    @Override
    public void update(BoardDto boardDto) {

    }

    @Override
    public void delete(BoardDto boardDto) {

    }
}

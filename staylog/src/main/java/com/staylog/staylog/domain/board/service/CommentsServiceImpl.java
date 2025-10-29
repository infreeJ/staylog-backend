package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.CommentsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {


    @Override
    public List<CommentsDto> getByBoardId(int boardId) {
        return List.of();
    }

    @Override
    public void insert(CommentsDto dto) {

    }

    @Override
    public void delete(CommentsDto dto) {

    }

    @Override
    public void update(CommentsDto dto) {

    }
}

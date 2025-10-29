package com.staylog.staylog.domain.board.service;


import com.staylog.staylog.domain.board.dto.LikesDto;
import com.staylog.staylog.domain.board.repository.LikesDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    // dao 의존 객체 주입
    //private final LikesDao dao;

    @Override
    public List<LikesDto> getByBoardId(int boardId) {
        return List.of();
    }

    @Override
    public void insert(LikesDto dto) {

    }

    @Override
    public void delete(LikesDto dto) {

    }

    @Override
    public int countByBoardId(int boardId) {
        return 0;
    }
}

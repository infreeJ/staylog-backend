package com.staylog.staylog.domain.board.service;


import com.staylog.staylog.domain.board.dto.LikesDto;
import com.staylog.staylog.domain.board.mapper.LikesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesMapper likesMapper;

    @Override
    public List<LikesDto> getByBoardId(long boardId) {

        return likesMapper.getByBoardId(boardId);
    }

    @Override
    public void insert(LikesDto dto) {

        likesMapper.insert(dto);
    }

    @Override
    public void delete(LikesDto dto) {

        likesMapper.delete(dto);
    }

    @Override
    public int countByBoardId(long boardId) {

        return likesMapper.countByBoardId(boardId);
    }

    @Override
    public int liked(long boardId, long userId) {

        return likesMapper.liked(boardId, userId);
    }
}

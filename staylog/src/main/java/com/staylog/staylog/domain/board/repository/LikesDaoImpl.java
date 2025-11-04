package com.staylog.staylog.domain.board.repository;

import com.staylog.staylog.domain.board.dto.LikesDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikesDaoImpl implements LikesDao{

    private final SqlSession session;

    public LikesDaoImpl(SqlSession session) {
        this.session = session;
    }

    @Override
    public int count(int boardId) {

        return 0;
    }

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
}

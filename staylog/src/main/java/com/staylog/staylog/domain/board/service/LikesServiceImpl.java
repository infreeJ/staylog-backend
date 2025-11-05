package com.staylog.staylog.domain.board.service;


import com.staylog.staylog.domain.board.dto.LikesDto;
import com.staylog.staylog.domain.board.mapper.BoardMapper;
import com.staylog.staylog.domain.board.mapper.LikesMapper;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;
import io.netty.handler.ssl.IdentityCipherSuiteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesMapper likesMapper;
    private final BoardMapper boardMapper;

    @Override
    public List<LikesDto> getByBoardId(long boardId) {

        return likesMapper.getByBoardId(boardId);
    }

    @Transactional
    public void toggleLike(LikesDto likesDto) {
        boolean liked = likesMapper.liked(likesDto.getBoardId(), likesDto.getUserId());

        if (!liked) {
            likesMapper.addLike(likesDto);
            boardMapper.increaseLikeCount(likesDto.getBoardId());
        } else {
            likesMapper.deleteLike(likesDto);
            boardMapper.decreaseLikeCount(likesDto.getBoardId());
        }
    }

//
//    @Override
//    public void addLike(LikesDto likesDto) {
//        boolean exists = likesMapper.liked(likesDto.getBoardId(), likesDto.getUserId());
//        if (exists) {
//            throw new BusinessException(ErrorCode.BOARD_ALREADY_EXISTS);
//        }else {
//            likesMapper.addLike(likesDto);
//            boardMapper.increaseLikeCount(likesDto.getBoardId());
//        }
//    }
//
//    @Override
//    public void deleteLike(LikesDto likesDto) {
//
//        likesMapper.deleteLike(likesDto);
//        boardMapper.decreaseLikeCount(likesDto.getBoardId());
//    }

    @Override
    public int countByBoardId(long boardId) {

        return likesMapper.countByBoardId(boardId);
    }

    @Override
    public boolean liked(long boardId, long userId) {

        return likesMapper.liked(boardId, userId);
    }
}

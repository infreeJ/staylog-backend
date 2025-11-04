package com.staylog.staylog.domain.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.board.dto.CommentsDto;
import com.staylog.staylog.domain.board.mapper.CommentsMapper;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsServiceImpl implements CommentsService {

    private final CommentsMapper commentsMapper;

    // 댓글 목록 조회
    @Override
    public List<CommentsDto> getByBoardId(Long boardId) {
        log.info("댓글 목록 조회 시작 : boardId = {}", boardId);

        List<CommentsDto> comments = commentsMapper.getByBoardId(boardId);

        if (comments == null) {
            log.warn("댓글 목록이 없습니다 : boardId={}", boardId);
            throw new BusinessException(ErrorCode.COMMENTS_NOT_FOUND);
        }

        log.info("댓글 목록 조회 성공 - {}개", comments.size());
        return comments;
    }

    // 댓글 등록
    @Override
    public void insert(CommentsDto dto) {
        log.info("댓글 등록 시작 : boardId = {}, userId = {}", dto.getBoardId(), dto.getUserId());

        int rows = commentsMapper.insert(dto);

        if (rows == 0) {
            log.error("댓글 등록 실패 : boardId = {}", dto.getBoardId());
            throw new BusinessException(ErrorCode.COMMENTS_FAILED_CREATED);
        }

        log.info("댓글 등록 성공 : commentId = {}", dto.getCommentId());
    }

    // 댓글 수정
    @Override
    public void update(CommentsDto dto) {
        log.info("댓글 수정 시작 : commentId = {}", dto.getCommentId());

        int rows = commentsMapper.update(dto);

        if (rows == 0) {
            log.warn("댓글 수정 실패 : commentId = {}", dto.getCommentId());
            throw new BusinessException(ErrorCode.COMMENTS_NOT_FOUND);
        }

        log.info("댓글 수정 성공 : commentId = {}", dto.getCommentId());
    }

    // 댓글 논리 삭제
    @Override
    public void delete(CommentsDto dto) {
        log.info("댓글 삭제 시작 : commentId = {}", dto.getCommentId());

        int rows = commentsMapper.delete(dto);

        if (rows == 0) {
            log.warn("댓글 삭제 실패 : commentId = {}", dto.getCommentId());
            throw new BusinessException(ErrorCode.COMMENTS_NOT_FOUND);
        }

        log.info("댓글 삭제 성공 : commentId = {}", dto.getCommentId());
    }
}

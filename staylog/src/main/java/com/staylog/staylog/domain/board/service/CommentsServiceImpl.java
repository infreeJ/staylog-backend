package com.staylog.staylog.domain.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staylog.staylog.domain.board.mapper.BoardMapper;
import com.staylog.staylog.domain.notification.dto.request.NotificationRequest;
import com.staylog.staylog.domain.notification.dto.response.DetailsResponse;
import com.staylog.staylog.domain.notification.service.NotificationService;
import com.staylog.staylog.domain.user.dto.UserDto;
import com.staylog.staylog.domain.user.mapper.UserMapper;
import com.staylog.staylog.global.event.CommentCreatedEvent;
import com.staylog.staylog.global.security.jwt.JwtTokenProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.board.dto.CommentsDto;
import com.staylog.staylog.domain.board.mapper.CommentsMapper;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsServiceImpl implements CommentsService {

    private final CommentsMapper commentsMapper;
    private final ApplicationEventPublisher eventPublisher;

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
	@Transactional
	public void insert(CommentsDto commentsDto) {
		log.info("댓글 등록 시작 : boardId = {}, userId = {}", commentsDto.getBoardId(), commentsDto.getUserId());
	
		// 무한 대댓글 막기 (댓글과 답글까지만 가능하게)
		if (commentsDto.getParentId() != null) {
			CommentsDto targetComment = commentsMapper.getOneByCommentId(commentsDto.getParentId());
			if (targetComment != null) {
				// 최상위 댓글이 아니라면 parentId를 최상위 댓글로 변경
				if (!targetComment.getCommentId().equals(targetComment.getParentId())) {
					commentsDto.setParentId(targetComment.getParentId());
				}
			}
		}
		
		int rows = commentsMapper.insert(commentsDto);
		
		if (rows == 0) {
		    log.error("댓글 등록 실패 : boardId = {}", commentsDto.getBoardId());
		    throw new BusinessException(ErrorCode.COMMENTS_FAILED_CREATED);
		}
		
		log.info("댓글 등록 성공 : commentId = {}", commentsDto.getCommentId());
		
		// =================== 댓글 작성 이벤트 발행 (알림에서 사용) ======================
		CommentCreatedEvent event = new CommentCreatedEvent(
		commentsDto.getCommentId(),
		commentsDto.getUserId(),
		commentsDto.getBoardId()
		);
		eventPublisher.publishEvent(event);
	}

    // 댓글 수정
    @Override
    @Transactional
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

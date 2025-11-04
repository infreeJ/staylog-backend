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
import com.staylog.staylog.global.security.jwt.JwtTokenProvider;
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
    private final NotificationService notificationService;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    private final BoardMapper boardMapper;

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
    public void insert(CommentsDto commentsDto) {
        log.info("댓글 등록 시작 : boardId = {}, userId = {}", commentsDto.getBoardId(), commentsDto.getUserId());

        int rows = commentsMapper.insert(commentsDto);

        if (rows == 0) {
            log.error("댓글 등록 실패 : boardId = {}", commentsDto.getBoardId());
            throw new BusinessException(ErrorCode.COMMENTS_FAILED_CREATED);
        }

        log.info("댓글 등록 성공 : commentId = {}", commentsDto.getCommentId());

        // =================== 알림 추가 로직 ======================

        Long userId = boardMapper.getUserIdByBoardId(commentsDto.getBoardId()); // 알림 수취인 PK

        Optional<UserDto> userOpt = userMapper.findByUserId(commentsDto.getUserId());
        UserDto user = userOpt.get();
        String writerNickname = user.getNickname(); // 댓글 작성자 닉네임

        try {
            // Details 객체 구성
            DetailsResponse detailsResponse = DetailsResponse.builder()
                    .imageUrl("https://picsum.photos/id/10/200/300") // 댓글 작성자 프로필 이미지
                    .date(String.valueOf(LocalDateTime.now()))
                    .title(writerNickname) // 댓글 작성자 닉네임
                    .message(commentsDto.getContent()) // 댓글 내용
                    .typeName("Comment")
                    .build();
            
            // Details를 문자열로 직렬화
            String detailsString = objectMapper.writeValueAsString(detailsResponse);

            // Details를 삽입해서 DB에 저장 가능한 Request로 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(userId) // 알림 수취인 PK
                    .notiType("NOTI_NEW_COMMENT")
                    .targetId(commentsDto.getBoardId()) // 댓글이 작성된 게시글 PK
                    .details(detailsString)
                    .build();

            // 저장 및 푸시 메서드 호출 (DB 저장용 notificationRequest와 반복적인 objectMapper 사용을 피할 detailsResponse 전달)
            notificationService.saveAndPushNotification(notificationRequest, detailsResponse);
        } catch (Exception e) {
            log.error("알림 저장 및 푸시 실패. 롤백 방지하기 위해 throw는 생략됨");
        }
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

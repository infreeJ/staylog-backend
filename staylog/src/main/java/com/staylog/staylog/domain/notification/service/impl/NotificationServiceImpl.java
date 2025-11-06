package com.staylog.staylog.domain.notification.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staylog.staylog.domain.board.dto.CommentsDto;
import com.staylog.staylog.domain.board.mapper.BoardMapper;
import com.staylog.staylog.domain.board.mapper.CommentsMapper;
import com.staylog.staylog.domain.notification.dto.request.NotificationRequest;
import com.staylog.staylog.domain.notification.dto.request.NotificationSelectRequest;
import com.staylog.staylog.domain.notification.dto.request.ReadAllRequest;
import com.staylog.staylog.domain.notification.dto.request.ReadRequest;
import com.staylog.staylog.domain.notification.dto.response.*;
import com.staylog.staylog.domain.notification.mapper.NotificationMapper;
import com.staylog.staylog.domain.notification.service.NotificationService;
import com.staylog.staylog.domain.notification.service.SseService;
import com.staylog.staylog.domain.user.mapper.UserMapper;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.event.CommentCreatedEvent;
import com.staylog.staylog.global.event.ReviewCreatedEvent;
import com.staylog.staylog.global.event.SignupEvent;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;
    private final SseService sseService;
    private final UserMapper userMapper;
    private final CommentsMapper commentsMapper;
    private final BoardMapper boardMapper;

    /**
     * 리뷰 게시글 작성 이벤트리스너 메서드
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @Override
    @TransactionalEventListener
    public void handleReviewCreatedEvent(ReviewCreatedEvent event) {
        long recipientId = (long) 7;  // TODO: 원래 Admin에게 보내야하지만 개발 환경이라 infreeJ 아이디로 수취
        String nickname = userMapper.findNicknameByUserId(event.getUserId());
        String accommodationName = boardMapper.getAccommodationNameByBoardId(event.getBoardId());

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("https://picsum.photos/id/10/200/300")
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(nickname) // 리뷰 작성자 닉네임
                .message( "[" + accommodationName + "]" + " 신규 리뷰")
                .typeName("Review")
                .build();

        try {
            // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성
            String detailsObject = objectMapper.writeValueAsString(detailsResponse);

            // INSERT의 parameterType 객체 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(recipientId)
                    .notiType("NOTI_REVIEW_CREATE")
                    .targetId(event.getBoardId()) // 이동할 페이지 PK
                    .details(detailsObject)
                    .build();

            // DB 저장 후 SSE 요청 메서드 호출
            saveAndPushNotification(notificationRequest, detailsResponse);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 회원가입 이벤트리스너 메서드
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @Override
    @TransactionalEventListener
    public void handleSignupEvent(SignupEvent event) {

        long recipientId = event.getUserId();

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("https://picsum.photos/id/10/200/300")
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title("회원가입을 축하합니다!")
                .message("웰컴 쿠폰이 발급되었습니다.")
                .typeName("Signup")
                .build();

        try {
            // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성
            String detailsObject = objectMapper.writeValueAsString(detailsResponse);

            // INSERT의 parameterType 객체 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(recipientId)
                    .notiType("NOTI_SIGNUP")
                    .targetId(recipientId) // 이동할 페이지 PK
                    .details(detailsObject)
                    .build();

            // DB 저장 후 SSE 요청하는 메서드 호출
            saveAndPushNotification(notificationRequest, detailsResponse);


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    
    

    /**
     * 댓글 작성 이벤트리스너 메서드
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @Override
    @TransactionalEventListener
    public void handleCommentCreatedEvent(CommentCreatedEvent event) {

        String nickname = userMapper.findNicknameByUserId(event.getUserId()); // 댓글 작성자 닉네임
        CommentsDto commentsDto = commentsMapper.getOneByCommentId(event.getCommentId()); // 댓글 데이터
        long recipientId = boardMapper.getUserIdByBoardId(event.getBoardId()); // 댓글이 작성된 게시글 작성자의 PK

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("https://picsum.photos/id/10/200/300")
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(nickname) // 댓글 작성자 닉네임
                .message(commentsDto.getContent())
                .typeName("Comment")
                .build();

        try {
            // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성
            String detailsObject = objectMapper.writeValueAsString(detailsResponse);

            // INSERT의 parameterType 객체 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(recipientId)
                    .notiType("NOTI_COMMENT_CREATE")
                    .targetId(commentsDto.getBoardId()) // 이동할 페이지 PK
                    .details(detailsObject)
                    .build();

            // DB 저장 후 SSE 요청하는 메서드 호출
            saveAndPushNotification(notificationRequest, detailsResponse);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 알림 데이터 저장 및 푸시
     *
     * @param notificationRequest 알림 데이터 + JSON 형태의 String Type 데이터
     * @param detailsResponse     반복적인 직렬화, 역직렬화를 막기 위한 온전한 Details 객체(프론트에 그대로 출력 가능)
     * @apiNote 알림 별로 필요한 상세데이터는 해당하는 이벤트리스너에서
     * JSON 형식으로 각 타입별 데이터에 맞게 notificationRequest.details 필드에 담아 가져온다.
     * notificationRequest를 사용해서 DB에 저장 후 details 필드의 값을 사용해,
     * NotificationResponse를 구성하여 사용자에게 PUSH한다.
     * @author 이준혁
     */
    @Override
    public void saveAndPushNotification(NotificationRequest notificationRequest, DetailsResponse detailsResponse) {

        // DB 저장
        int success = notificationMapper.notiSave(notificationRequest);

        if (success == 1) {
            // 클라이언트에게 SSE로 푸시하기위한 객체 구성
            NotificationResponse notificationResponse = NotificationResponse.builder()
                    .notiId(notificationRequest.getNotiId()) // selectKey로 채워진 알림 PK
                    .targetId(notificationRequest.getTargetId()) // 페이지 이동을 위한 PK
                    .details(detailsResponse)
                    .isRead("N")
                    .createdAt(LocalDateTime.now())
                    .build();

            // SSE Push 메서드 호출
            sseService.sendNotification(notificationRequest.getUserId(), notificationResponse);
        }
    }


    /**
     * 유저 한명의 알림 리스트 조회
     *
     * @param userId 유저 PK
     * @return List<NotificationResponse>
     * @author 이준혁
     */
    @Override
    public List<NotificationResponse> getNotificationList(long userId) {

        // 유저의 알림 리스트 조회
        List<NotificationSelectRequest> notiListFromDb = notificationMapper.findNotificationsByUserId(userId);

        if (notiListFromDb == null || notiListFromDb.isEmpty()) {
            log.warn("알림 데이터 조회 실패: 알림 정보를 찾을 수 없습니다. - userId={}", userId);
            throw new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }

        // map으로 순환하며 프론트에서 바로 사용할 수 있는 JSON으로 가공
        return notiListFromDb.stream().map((notiData) -> {

            // DetailsResponse 타입으로 역직렬화
            DetailsResponse detailsObject;
            try {
                detailsObject = objectMapper.readValue(
                        notiData.getDetails(),
                        DetailsResponse.class
                );
            } catch (Exception e) {
                log.error("목록 조회용 JSON 역직렬화 실패: {}", notiData.getDetails(), e);
                detailsObject = null; // (혹은 new DetailsResponse() 빈 객체)
            }

            // 새로운 JSON 객체 조합
            return NotificationResponse.builder()
                    .notiId(notiData.getNotiId()) // selectKey로 가져온 PK
                    .targetId(notiData.getTargetId())
                    .isRead(notiData.getIsRead())
                    .createdAt(notiData.getCreatedAt())
                    .details(detailsObject) // 변환된 객체
                    .build();
        }).toList();
    }


    /**
     * 알림 삭제
     *
     * @param notiId 알림 PK
     * @author 이준혁
     */
    @Override
    public void deleteNotification(long notiId) {
        int isSuccess = notificationMapper.deleteByNotiId(notiId);

        if (isSuccess == 0) {
            log.warn("알림 데이터 삭제 실패: 알림 정보를 찾을 수 없습니다. - notiId={}", notiId);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        }
    }

    /**
     * 단일 알림 읽음 처리
     *
     * @param readRequest 알림 PK
     * @author 이준혁
     */
    @Override
    public void readOne(ReadRequest readRequest) {
        int isSuccess = notificationMapper.readOne(readRequest);

        if (isSuccess == 0) {
            log.warn("알림 데이터 읽음 처리 실패: 알림 정보를 찾을 수 없습니다. - notiId={}", readRequest);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        }
    }

    /**
     * 모든 알림 읽음 처리
     *
     * @param readAllRequest 유저 PK
     * @author 이준혁
     */
    @Override
    public void readAll(ReadAllRequest readAllRequest) {
        int isSuccess = notificationMapper.readAll(readAllRequest);

        if (isSuccess == 0) {
            log.warn("알림 데이터 읽음 처리 실패: 알림 정보를 찾을 수 없습니다. - userId={}", readAllRequest);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        }
    }


    /**
     * 안읽은 알림 수 조회
     *
     * @param userId 사용자 PK
     * @return 안읽은 알림 수
     */
    @Override
    public int unreadCount(long userId) {
        return notificationMapper.unreadCount(userId);
    }


}

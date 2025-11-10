package com.staylog.staylog.domain.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staylog.staylog.domain.notification.dto.request.NotificationRequest;
import com.staylog.staylog.domain.notification.dto.request.NotificationSelectRequest;
import com.staylog.staylog.domain.notification.dto.request.ReadAllRequest;
import com.staylog.staylog.domain.notification.dto.request.ReadRequest;
import com.staylog.staylog.domain.notification.dto.response.*;
import com.staylog.staylog.domain.notification.mapper.NotificationMapper;
import com.staylog.staylog.domain.notification.service.NotificationService;
import com.staylog.staylog.domain.notification.service.SseService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.event.NotificationCreatedAllEvent;
import com.staylog.staylog.global.event.NotificationCreatedEvent;
import com.staylog.staylog.global.exception.BusinessException;
import com.staylog.staylog.global.exception.custom.InfrastructureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;
    private final SseService sseService;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * 알림 데이터 저장
     *
     * @param notificationRequest 알림 데이터 + JSON 형태의 String Type 데이터
     * @param detailsResponse     반복적인 직렬화, 역직렬화를 막기 위한 온전한 Details 객체(프론트에 그대로 출력 가능)
     * @apiNote 알림 별로 필요한 상세데이터는 해당하는 이벤트리스너에서
     * JSON 형식으로 각 타입별 데이터에 맞게 notificationRequest.details 필드에 담아 가져온다.
     * notificationRequest를 사용해서 DB에 저장 후 details 필드의 값을 사용해,
     * NotificationResponse를 구성하여 SSE에 전달한다.
     * @author 이준혁
     */
    @Override
    @Transactional
    public void saveNotification(NotificationRequest notificationRequest, DetailsResponse detailsResponse) {

        try {
            // DB 저장
            int success = notificationMapper.notiSave(notificationRequest);
            if (success == 0) {
                log.error("알림 DB 저장 0건, 로직 확인 필요. request: {}", notificationRequest); // TODO: 너무 긴 로그 -> 수정필요
                throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
            }
        } catch (TransientDataAccessException e) { // @Retryable이 가로채서 재시도
            log.warn("일시적인 DB 오류 발생", e);
            throw new InfrastructureException(ErrorCode.TEMPORARY_SERVER_ERROR);
        } catch (DataIntegrityViolationException e) {
            log.error("알림 저장 데이터 무결성 오류", e);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        } catch (DataAccessException e) {
            log.error("알 수 없는 DB 오류 발생", e);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        }

        log.info("알림 데이터 저장 완료. notiId: {}", notificationRequest.getNotiId());


        // 클라이언트에게 SSE로 푸시하기위한 객체 구성
        NotificationResponse notificationResponse = NotificationResponse.builder()
                .notiId(notificationRequest.getNotiId()) // selectKey로 채워진 알림 PK
                .targetId(notificationRequest.getTargetId()) // 페이지 이동을 위한 PK
                .details(detailsResponse)
                .isRead("N")
                .createdAt(LocalDateTime.now())
                .build();


        // =========== 알림 저장 이벤트 발행(SSE 푸시) ==============
        NotificationCreatedEvent event = new NotificationCreatedEvent(notificationRequest.getUserId(), notificationResponse);
        eventPublisher.publishEvent(event);
        // ======================================================

    }


    /**
     * 전체 사용자 일괄 알림 데이터 저장
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
    @Transactional
    public void saveAllNotification(NotificationRequest notificationRequest, DetailsResponse detailsResponse) {

        try {
            // Users 테이블에서 userId를 매핑하여 모든 사용자에게 알림 저장
            int success = notificationMapper.notiSaveBatchFromUsers(notificationRequest);
            if(success == 0) {
                log.error("알림 DB 저장 0건, 로직 확인 필요. request: {}", notificationRequest); // TODO: 너무 긴 로그 -> 수정필요
                throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
            }
        } catch (TransientDataAccessException e) { // @Retryable이 가로채서 재시도
            log.warn("일시적인 DB 오류 발생", e);
            throw new InfrastructureException(ErrorCode.TEMPORARY_SERVER_ERROR);
        } catch (DataIntegrityViolationException e) {
            log.error("알림 저장 데이터 무결성 오류", e);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        } catch (DataAccessException e) {
            log.error("알 수 없는 DB 오류 발생", e);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        }

        log.info("알림 데이터 일괄 저장 완료. batchId: {}", notificationRequest.getBatchId());

        // 클라이언트에게 SSE로 푸시하기위한 객체 구성
        NotificationResponse notificationResponse = NotificationResponse.builder()
                .notiId(null) // 단체 저장이므로 현재 null
                .targetId(null) // 단체 저장이므로 현재 null
                .details(detailsResponse)
                .isRead("N")
                .createdAt(LocalDateTime.now())
                .build();

        // =========== 단체 알림 저장 이벤트 발행(SSE 푸시) ==============
        NotificationCreatedAllEvent event = new NotificationCreatedAllEvent(notificationResponse, notificationRequest.getBatchId());
        eventPublisher.publishEvent(event);
        // ======================================================
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
            log.warn("알림 데이터 조회 실패: 알림이 없거나, 알림 정보를 찾을 수 없습니다. - userId={}", userId);
//            throw new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND);
            return Collections.emptyList(); // 프론트의 catch를 실행시키지 않도록 빈 List를 반환
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
     * 해당 유저의 알림 전체 삭제
     *
     * @param userId 유저 PK
     * @author 이준혁
     */
    public void deleteNotificationAll(long userId) {
        try {
            notificationMapper.deleteAllByUserId(userId);
        } catch (BusinessException e) {
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

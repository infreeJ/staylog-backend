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
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;
    private final SseService sseService;


    /**
     * 알림 데이터 저장 및 푸시
     * @param notificationRequest 알림 데이터 + JSON 형태의 String Type 데이터
     * @apiNote 알림 별로 필요한 상세데이터는 해당하는 서비스 로직에서
     * JSON 형식으로 각 타입별 데이터에 맞게 notificationRequest.details 필드에 담아 가져온다.
     * notificationRequest를 사용해서 DB에 저장 후 details 필드의 값을 사용해,
     * NotificationResponse를 구성하여 사용자에게 PUSH한다.
     * @author 이준혁
     */
    @Override
    public void saveAndPushNotification(NotificationRequest notificationRequest) {
        int isSuccess = notificationMapper.notiSave(notificationRequest);

        if (isSuccess == 0) {
            log.warn("알림 데이터 저장 실패: 잘못된 알림 데이터 - notificationRequest={}", notificationRequest);
            // 여기서 throw를 던지면 롤백 발생하므로 X
            // throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        }

        // DetailsResponse 타입으로 역직렬화
        DetailsResponse detailsObject;
        try {
            detailsObject = objectMapper.readValue(
                    notificationRequest.getDetails(),
                    DetailsResponse.class
            );
        } catch (Exception e) {
            log.error("PUSH용 JSON 역직렬화 실패: {}", notificationRequest.getDetails(), e);
            detailsObject = null; // (혹은 new DetailsResponse() 빈 객체)
        }

        // NotificationResponse 구성
        NotificationResponse notificationResponse = NotificationResponse.builder()
                .notiId(notificationRequest.getNotiId()) // selectKey로 가져온 PK
                .targetId(notificationRequest.getTargetId())
                .isRead("N")
                .createdAt(LocalDateTime.now())
                .details(detailsObject) // 변환된 객체
                .build();

        // Sse 푸시 메서드 호출
        sseService.sendNotification(notificationRequest.getUserId(), notificationResponse);
    }


    /**
     * 유저 한명의 알림 리스트 조회
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
     * @author 이준혁
     * @param readRequest 알림 PK
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
     * @author 이준혁
     * @param readAllRequest 유저 PK
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
     * @param userId 사용자 PK
     * @return 안읽은 알림 수
     */
    @Override
    public int unreadCount(long userId) {
        return notificationMapper.unreadCount(userId);
    }


}

package com.staylog.staylog.domain.notification.service.impl;


import com.staylog.staylog.domain.notification.dto.request.NotificationRequest;
import com.staylog.staylog.domain.notification.dto.request.ReadRequest;
import com.staylog.staylog.domain.notification.dto.response.NotificationResponse;
import com.staylog.staylog.domain.notification.mapper.NotificationMapper;
import com.staylog.staylog.domain.notification.service.NotificationService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    /**
     * 알림 데이터 저장
     * 알림 별로 필요한 상세데이터는 해당하는 서비스 로직에서
     * JSON 형식으로 notificationRequest.details 필드에 담아 가져온다.
     * @author 이준혁
     * @param notificationRequest 알림 정보
     */
    @Override
    public void createNotification(NotificationRequest notificationRequest) {
        int isSuccess = notificationMapper.notiSave(notificationRequest);

        if(isSuccess == 0) {
            log.warn("알림 데이터 저장 실패: 잘못된 알림 데이터 - notificationRequest={}", notificationRequest);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        }
    }

    /**
     * 유저 한명의 알림 리스트 조회
     * @author 이준혁
     * @param userId 유저 PK
     * @return List<NotificationResponse>
     */
    @Override
    public List<NotificationResponse> getNotificationList(long userId) {

        List<NotificationResponse> notiList = notificationMapper.findNotificationsByUserId(userId);

        if(notiList == null) {
            log.warn("알림 데이터 조회 실패: 알림 정보를 찾을 수 없습니다. - userId={}", userId);
            throw new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
        return notiList;
    }


    /**
     * 알림 삭제
     * @author 이준혁
     * @param notiId 알림 PK
     */
    @Override
    public void deleteNotification(long notiId) {
        int isSuccess = notificationMapper.deleteByNotiId(notiId);

        if(isSuccess == 0) {
            log.warn("알림 데이터 삭제 실패: 알림 정보를 찾을 수 없습니다. - notiId={}", notiId);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        }
    }

    /**
     * 알림 읽음 처리
     * @author 이준혁
     * @param readRequest 알림 PK
     */
    @Override
    public void readNotification(ReadRequest readRequest) {
        int isSuccess = notificationMapper.readNotification(readRequest);

        if(isSuccess == 0) {
            log.warn("알림 데이터 읽음 처리 실패: 알림 정보를 찾을 수 없습니다. - notiId={}", readRequest);
            throw new BusinessException(ErrorCode.NOTIFICATION_FAILED);
        }
    }
}

package com.staylog.staylog.domain.notification.service;

import com.staylog.staylog.domain.notification.dto.request.NotificationRequest;
import com.staylog.staylog.domain.notification.dto.request.ReadAllRequest;
import com.staylog.staylog.domain.notification.dto.request.ReadRequest;
import com.staylog.staylog.domain.notification.dto.response.NotificationResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {


    /**
     * 알림 데이터 저장 및 푸시
     * @author 이준혁
     * @param notificationRequest 알림 정보
     */
    public void saveAndPushNotification(NotificationRequest notificationRequest);


    /**
     * 유저 한명의 알림 리스트 조회
     * @author 이준혁
     * @param userId 유저 PK
     * @return List<NotificationResponse>
     */
    public List<NotificationResponse> getNotificationList(long userId);


    /**
     * 알림 삭제
     * @author 이준혁
     * @param notiId 알림 PK
     */
    public void deleteNotification(long notiId);


    /**
     * 단일 알림 읽음 처리
     * @author 이준혁
     * @param readRequest 알림 PK
     */
    public void readOne(ReadRequest readRequest);

    /**
     * 모든 알림 읽음 처리
     * @author 이준혁
     * @param readAllRequest 유저 PK
     */
    public void readAll(ReadAllRequest readAllRequest);

    /**
     * 안읽은 알림 수 조회
     * @param userId 사용자 PK
     * @return 안읽은 알림 수
     */
    public int unreadCount(long userId);
}

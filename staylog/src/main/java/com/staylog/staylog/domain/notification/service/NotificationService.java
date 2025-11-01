package com.staylog.staylog.domain.notification.service;

import com.staylog.staylog.domain.notification.dto.request.NotificationRequest;
import com.staylog.staylog.domain.notification.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {


    /**
     * 알림 데이터 저장
     * @author 이준혁
     * @param notificationRequest 알림 정보
     */
    public void createNotification(NotificationRequest notificationRequest);


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
}

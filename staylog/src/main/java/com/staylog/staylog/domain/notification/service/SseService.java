package com.staylog.staylog.domain.notification.service;

import com.staylog.staylog.domain.notification.dto.response.NotificationResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {


    /**
     * 클라이언트 구독 메서드
     * @param token AccessToken
     * @return SseEmitter
     * @author 이준혁
     */
    public SseEmitter subscribe(String token);


    public void sendNotification(Long userId, NotificationResponse data);
}

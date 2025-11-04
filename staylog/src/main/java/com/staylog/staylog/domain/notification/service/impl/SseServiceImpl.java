package com.staylog.staylog.domain.notification.service.impl;

import com.staylog.staylog.domain.notification.dto.response.NotificationResponse;
import com.staylog.staylog.domain.notification.service.SseService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;
import com.staylog.staylog.global.security.jwt.JwtTokenProvider;
import com.staylog.staylog.global.security.jwt.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {

    // Emitter 타임아웃 시간 (30분)
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;

    // UserId, SeeEmitter를 key-value 형태로 가지는 맵
    // 동시성 처리를 위한 ConcurrentHashMap
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * 클라이언트 구독 메서드
     * @param userId 인증된 사용자 PK
     * @return SseEmitter
     * @author 이준혁
     */
    @Override
    public SseEmitter subscribe(Long userId) {

        // SseEmitter를 생성하고 Map에 저장
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitters.put(userId, emitter);

        // 완료, 타임아웃, 에러 발생 시 해당 emitter 제거
        emitter.onCompletion(() -> this.emitters.remove(userId));
        emitter.onTimeout(() -> this.emitters.remove(userId));
        emitter.onError((e) -> this.emitters.remove(userId));

        try { // 연결 성공 및 더미 데이터 전송
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected! (userId: " + userId + ")"));
        } catch (IOException e) {
            log.warn("알림 채널 구독 실패 - userId={}", userId);
            throw new BusinessException(ErrorCode.NOTIFICATION_SUBSCRIBE_FAILED);
        }

        return emitter;
    }


    /**
     * 특정 유저에게 알림 푸시 메서드
     * @author 이준혁
     * @apiNote NotificationServiceImpl에서 호출하여 사용한다.
     * IOException은 해당 메서드에서 처리
     * @param userId 알림을 받을 유저의 PK
     * @param notificationResponse 프론트에서 즉시 사용할 수 있는 형태의 JSON 객체
     */
    @Override
    public void sendNotification(Long userId, NotificationResponse notificationResponse) {
        SseEmitter emitter = emitters.get(userId);

        if (emitter != null) {
            try { // new-notification 이벤트로 JSON으로 변환된 알림 전송
                emitter.send(SseEmitter.event()
                        .name("new-notification")
                        .data(notificationResponse));
            } catch (IOException e) {
                // 클라이언트 연결이 끊겼을 경우 제거
                emitters.remove(userId);
                log.warn("유효하지 않은 Emitter - userId={}", userId);
                // 여기서 throw를 던지면 롤백 발생하므로 X
                // throw new BusinessException(ErrorCode.NOTIFICATION_EMITTER_NOT_FOUND);
            }
        }
    }
}

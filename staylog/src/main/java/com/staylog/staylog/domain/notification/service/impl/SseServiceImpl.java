package com.staylog.staylog.domain.notification.service.impl;

import com.staylog.staylog.domain.notification.dto.response.NotificationResponse;
import com.staylog.staylog.domain.notification.dto.response.NotificationUserMapping;
import com.staylog.staylog.domain.notification.mapper.NotificationMapper;
import com.staylog.staylog.domain.notification.service.SseService;
import com.staylog.staylog.global.annotation.CommonRetryable;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.event.NotificationCreatedAllEvent;
import com.staylog.staylog.global.event.NotificationCreatedEvent;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private final NotificationMapper notificationMapper;

    /**
     * 클라이언트 구독 메서드
     *
     * @param userId 인증된 사용자 PK
     * @return SseEmitter
     * @author 이준혁
     */
    @Override
    public SseEmitter subscribe(Long userId) {
        log.info("SSE 연결 시작. userId: {}", userId);

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
            log.info("SSE 연결 성공. userId: {}, eventName: {}", userId, "connect");
        } catch (IOException e) {
            log.warn("알림 채널 구독 실패 - userId={}", userId);
            emitter.completeWithError(e);
        }
        return emitter;
    }


    /**
     * 특정 유저에게 알림 푸시 메서드
     * 알림 저장 이벤트를 받아 실행된다.
     *
     * @param event -> userId, notificationResponse
     * @author 이준혁
     * @apiNote NotificationServiceImpl에서 호출하여 사용한다.
     * IOException은 해당 메서드에서 처리
     */
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void sendNotification(NotificationCreatedEvent event) {
        long userId = event.getUserId();
        log.info("알림 발송 이벤트 확인 userId: {}", userId);

        // Map에서 해당 유저의 emitter를 꺼내기
        SseEmitter emitter = emitters.get(userId);

        if (emitter != null) {
            try { // new-notification 이벤트로 JSON으로 변환된 알림 전송
                emitter.send(SseEmitter.event()
                        .name("new-notification")
                        .data(event.getNotificationResponse()));
                log.info("알림 푸시 완료. userId: {}, eventName: {}", userId, "new-notification");
            } catch (IOException e) {
                // 클라이언트 연결이 끊겼을 경우 제거
                emitters.remove(userId);
                log.warn("유효하지 않은 Emitter - userId={}", userId);
                // 클라이언트가 창을 닫는 등 심각하지 않은 예외 -> throw없이 emitter만 제거
                // throw new BusinessException(ErrorCode.NOTIFICATION_EMITTER_NOT_FOUND);
            }
        }
    }


//    /**
//     * 현재 접속된 전체 사용자에게 알림을 보내는 메서드
//     * 알림 일괄 저장 이벤트를 받아 실행된다.
//     *
//     * @param event -> notificationResponse 알림 데이터
//     * @author 이준혁
//     */
//    @TransactionalEventListener
//    @CommonRetryable // 실패시 재시도
//    public void broadcast(NotificationCreatedAllEvent event) {
//        emitters.forEach((userId, emitter) -> {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("new-notification")
//                        .data(event.getNotificationResponse()));
//            } catch (IOException e) {
//                emitters.remove(userId);
//                log.warn("Broadcast 중 유효하지 않은 Emitter 발견 - userId={}", userId);
//            }
//        });
//    }

    /**
     * 일괄 저장된 알림 중 현재 접속자에게만 개별 푸시
     *
     * @param event -> notificationResponse 알림 데이터
     * @author 이준혁
     */
    @TransactionalEventListener
    @CommonRetryable
    public void pushBatchNotifications(NotificationCreatedAllEvent event) {
        log.info("일괄 알림 발송 이벤트 확인 batchId: {}", event.getBatchId());

        // 현재 접속 중인 유저 ID 목록
        Set<Long> connectedUserIds = getConnectedUserIds();
        if (connectedUserIds.isEmpty()) {
            log.info("접속 중인 유저가 없으므로 Batch SSE 푸시를 스킵합니다. batchId: {}", event.getBatchId());
            return;
        }

        // DB에서 방금 저장한 알림 중 접속 중인 유저의 것만 조회
        List<NotificationUserMapping> notisToPush = notificationMapper.findByBatchIdAndUserIds(event.getBatchId(), connectedUserIds);
        if (notisToPush == null || notisToPush.isEmpty()) {
            log.warn("알림 데이터 조회 실패: 접속한 유저가 없거나, 알림 정보를 찾을 수 없습니다.");
            throw new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }

        // 여러번 사용하므로 미리 꺼내놓기
        NotificationResponse responseTemplate = event.getNotificationResponse();

        for (NotificationUserMapping noti : notisToPush) {
            // 동시성 문제 발생 위험 -> 객체를 변경하지 않고 새로운 객체를 만들어서 PUSH
            // event.getNotificationResponse().setNotiId(noti.getNotiId());

            // notiId와 targetId만 DB에서 꺼내온 것으로 빌드하여 객체 생성
            NotificationResponse notificationResponse = NotificationResponse.builder()
                    .notiId(noti.getNotiId()) // (From DB)
                    .targetId(noti.getUserId()) // (From DB)
                    .details(responseTemplate.getDetails()) // (From Event Template)
                    .isRead(responseTemplate.getIsRead()) // (From Event Template)
                    .createdAt(responseTemplate.getCreatedAt()) // (From Event Template)
                    .build();

            // userId로 emitter를 꺼내서 이벤트 발행
            long userId = noti.getUserId();
            SseEmitter emitter = emitters.get(userId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("new-notification")
                            .data(notificationResponse));

                } catch (IOException e) {
                    emitters.remove(userId);
                    log.warn("Broadcast 중 유효하지 않은 Emitter 발견 - userId={}", userId);
                    // 클라이언트가 창을 닫는 등 심각하지 않은 예외 -> throw없이 emitter만 제거
                    // throw new BusinessException(ErrorCode.NOTIFICATION_EMITTER_NOT_FOUND);
                }
            }
        }
        log.info("일괄 알림 푸시 완료. eventName: {}", "new-notification");
    }


    private Set<Long> getConnectedUserIds() {
        // emitters 맵의 키셋(KeySet)을 반환합니다.
        // Set.copyOf()를 사용하면 불변(immutable) Set으로 복사해서 반환하므로,
        // 반환된 Set을 조작해도 원본 emitters 맵에 영향이 없어 더 안전합니다.
        return Set.copyOf(emitters.keySet());
    }


    /**
     * SSE 연결 타임아웃 방지용 Heartbeat 전송
     *
     * @author 이준혁
     * 20초마다 주석(comment)를 전송하여 타임아웃을 방지
     */
    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = 20000) // 20초
    public void sendHeartbeat() {
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().comment("keep-alive"));
                log.debug("SSE의 심장이 도키도키...(Heartbeat) userId: {}", userId);
            } catch (IOException e) {
                emitters.remove(userId);
                log.warn("Heartbeat 전송 실패로 SSE 연결 종료. userId: {}", userId);
                // throw를 던지지 않고 해당 유저만 조용히 처리
                // throw new RuntimeException(e);
            }
        });
    }



    /**
     * 애플리케이션 종료 시 연결된 SSE Emitter 연결을 정리하는 메서드
     * @author 이준혁
     */
    @EventListener(ContextClosedEvent.class)
    public void handleContextClosedEvent() {
        log.info("Spring Context Closed: 모든 SSE Emitter 연결을 종료합니다.");
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.complete();
            } catch (Exception e) {
                log.warn("SSE Emitter 종료 중 오류 발생. userId: {}", userId, e);
            }
        });
        emitters.clear();
        log.info("모든 SSE Emitter 연결 종료 완료.");
    }


}




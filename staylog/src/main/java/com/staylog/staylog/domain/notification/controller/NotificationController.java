package com.staylog.staylog.domain.notification.controller;

import com.staylog.staylog.domain.notification.dto.request.ReadRequest;
import com.staylog.staylog.domain.notification.dto.response.NotificationResponse;
import com.staylog.staylog.domain.notification.service.NotificationService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "NotificationController", description = "알림 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final MessageUtil messageUtil;


    /**
     * 유저 한명의 알림 리스트 조회
     * @author 이준혁
     * @param userId 유저 PK
     * @return List<NotificationResponse>
     */
    @Operation(summary = "알림 목록 조회", description = "로그인한 사용자의 모든 알림 리스트를 조회합니다.")
    @GetMapping("/notification/{userId}")
    public ResponseEntity<SuccessResponse<List<NotificationResponse>>> getNotificationList(@PathVariable long userId) {

        List<NotificationResponse> notiList = notificationService.getNotificationList(userId);
        String message = messageUtil.getMessage(SuccessCode.NOTIFICATION_LIST_FIND.getMessageKey());
        String code = SuccessCode.NOTIFICATION_LIST_FIND.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, notiList));
    }

    
    /**
     * 알림 삭제
     * @author 이준혁
     * @param notiId 알림 PK
     */
    @Operation(summary = "알림 삭제", description = "알림을 삭제합니다.")
    @DeleteMapping("/notification/{notiId}/delete")
    public ResponseEntity<SuccessResponse<Void>> deleteNotification(@PathVariable long notiId) {
        notificationService.deleteNotification(notiId);

        String message = messageUtil.getMessage(SuccessCode.NOTIFICATION_DELETE.getMessageKey());
        String code = SuccessCode.NOTIFICATION_DELETE.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }


    /**
     * 알림 읽음 처리
     * @author 이준혁
     * @param readRequest 알림 PK
     */
    @Operation(summary = "알림 읽음 처리", description = "알림의 읽음 상태를 Y로 변경합니다.")
    @PatchMapping("/notification/read")
    public ResponseEntity<SuccessResponse<Void>> readNotification(@RequestBody ReadRequest readRequest) {
        notificationService.readNotification(readRequest);

        String message = messageUtil.getMessage(SuccessCode.NOTIFICATION_READ.getMessageKey());
        String code = SuccessCode.NOTIFICATION_READ.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }


    /**
     * 클라이언트 구독 컨트롤러 메서드
     * @author 이준혁
     * @param token AccessToken
     * @return SseEmitter
     */
    @Operation(summary = "클라이언트 SSE 채널 구독", description = "로그인한 사용자의 토큰을 검증하여 SSE 채널에 구독시킵니다.")
    @GetMapping(value = "/v1/notification/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam String token) {

        return notificationService.subscribe(token);
    }
    



}

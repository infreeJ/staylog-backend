package com.staylog.staylog.domain.notification.controller;

import com.staylog.staylog.domain.notification.dto.response.NotificationResponse;
import com.staylog.staylog.domain.notification.service.NotificationService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/notification/{notiId}/delete")
    public ResponseEntity<SuccessResponse<Void>> deleteNotification(long notiId) {
        notificationService.deleteNotification(notiId);

        String message = messageUtil.getMessage(SuccessCode.NOTIFICATION_DELETE.getMessageKey());
        String code = SuccessCode.NOTIFICATION_DELETE.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }



}

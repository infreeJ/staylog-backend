package com.staylog.staylog.domain.notification.controller;

import com.staylog.staylog.domain.notification.service.SseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "NotificationController", description = "알림 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1")
@Slf4j
public class SseController {

    private final SseService sseService;

    /**
     * 클라이언트 구독 컨트롤러 메서드
     * @author 이준혁
     * @param token AccessToken
     * @return SseEmitter
     */
    @Operation(summary = "클라이언트 SSE 채널 구독", description = "로그인한 사용자의 토큰을 검증하여 SSE 채널에 구독시킵니다.")
    @GetMapping(value = "/notification/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam String token) {

        return sseService.subscribe(token);
    }
}

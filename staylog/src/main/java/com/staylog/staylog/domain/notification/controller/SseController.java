package com.staylog.staylog.domain.notification.controller;

import com.staylog.staylog.domain.notification.service.SseService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;
import com.staylog.staylog.global.security.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SseController", description = "SSE API")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1")
@Slf4j
public class SseController {

    private final SseService sseService;

    /**
     * 클라이언트 구독 컨트롤러 메서드
     * @author 이준혁
     * @param user 인증된 유저 객체
     * @return SseEmitter
     */
    @Operation(summary = "클라이언트 SSE 채널 구독", description = "로그인한 사용자의 토큰을 검증하여 SSE 채널에 구독시킵니다.")
    @GetMapping(value = "/notification/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal SecurityUser user) {
    	Long userId = user.getUserId();
    	System.out.println("SSE 연결 시도 | userID: " + userId);

        if (userId == null) {
            // 로직상 필터 401를 뱉지만 만약을 대비한 방어
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        return sseService.subscribe(userId);
    }
}

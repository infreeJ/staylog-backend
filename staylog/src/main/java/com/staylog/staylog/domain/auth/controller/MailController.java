package com.staylog.staylog.domain.auth.controller;

import com.staylog.staylog.domain.auth.dto.request.MailCheckRequest;
import com.staylog.staylog.domain.auth.dto.request.MailSendRequest;
import com.staylog.staylog.domain.auth.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "MailController", description = "이메일 인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MailController {

    private final MailService mailService;

    @Operation(summary = "인증 메일 전송", description = "인증 메일을 사용자에게 전송합니다. \n * 인증 코드는 10분 동안 유효합니다.")
    @PostMapping("/mail-send")
    public ResponseEntity<Map<String, String>> sendVerificationMail(@RequestBody MailSendRequest requestDto) {
        mailService.sendVerificationMail(requestDto.getEmail());
        return ResponseEntity.ok(Map.of("message", "인증 메일이 성공적으로 발송되었습니다."));
    }
    
    
    @Operation(summary = "인증 코드 검증", description = "사용자가 입력한 인증 코드를 검증합니다.")
    @PostMapping("/mail-check")
    public ResponseEntity<Map<String, String>> checkVerificationMail(@RequestBody MailCheckRequest requestDto) {
        boolean isVerified = mailService.verifyMail(requestDto.getEmail(), requestDto.getCode());
        if (isVerified) {
            return ResponseEntity.ok(Map.of("message", "이메일 인증에 성공했습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "인증번호가 유효하지 않습니다."));
        }
    }
}

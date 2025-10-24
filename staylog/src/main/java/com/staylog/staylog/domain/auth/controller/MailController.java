package com.staylog.staylog.domain.auth.controller;

import com.staylog.staylog.domain.auth.dto.request.MailCheckRequest;
import com.staylog.staylog.domain.auth.dto.request.MailSendRequest;
import com.staylog.staylog.domain.auth.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MailController {

    private final MailService mailService;


    @PostMapping("/mail-send")
    public ResponseEntity<Map<String, String>> sendVerificationMail(@RequestBody MailSendRequest requestDto) {
        mailService.sendVerificationMail(requestDto.getEmail());
        return ResponseEntity.ok(Map.of("message", "인증 메일이 성공적으로 발송되었습니다."));
    }

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

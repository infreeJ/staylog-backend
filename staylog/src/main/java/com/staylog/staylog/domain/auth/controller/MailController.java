package com.staylog.staylog.domain.auth.controller;

import com.staylog.staylog.domain.auth.dto.request.MailCheckRequest;
import com.staylog.staylog.domain.auth.dto.request.MailSendRequest;
import com.staylog.staylog.domain.auth.dto.response.MailCheckResponse;
import com.staylog.staylog.domain.auth.dto.response.MailSendResponse;
import com.staylog.staylog.domain.auth.service.MailService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "MailController", description = "이메일 인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Slf4j
public class MailController {

    private final MailService mailService;
    private final MessageUtil messageUtil;

    /**
     * 인증 메일 전송 컨트롤러
     * @author 이준혁
     * @param mailSendRequest 이메일을 담을 Dto
     * @return 이메일, 만료시간 반환
     */
    @Operation(summary = "인증 메일 전송", description = "인증 메일을 사용자에게 전송합니다. \n * 인증 코드는 10분 동안 유효합니다.")
    @PostMapping("/mail-send")
    public ResponseEntity<SuccessResponse<MailSendResponse>> sendVerificationMail(@RequestBody MailSendRequest mailSendRequest) {
        LocalDateTime expiresAt = mailService.sendVerificationMail(mailSendRequest.getEmail());

        MailSendResponse data = MailSendResponse.of(mailSendRequest.getEmail(), expiresAt);
        String message = messageUtil.getMessage(SuccessCode.MAIL_SENT.getMessageKey());

        return ResponseEntity.ok(SuccessResponse.of(message, data));
    }

    /**
     * 인증 코드 검증 컨트롤러
     * @author 이준혁
     * @param mailCheckRequest 이메일과 인증코드를 담을 Dto
     * @return 이메일 반환
     */
    @Operation(summary = "인증 코드 검증", description = "사용자가 입력한 인증 코드를 검증합니다.")
    @PostMapping("/mail-check")
    public ResponseEntity<SuccessResponse<MailCheckResponse>> checkVerificationMail(@RequestBody MailCheckRequest mailCheckRequest) {
        boolean isVerified = mailService.verifyMail(mailCheckRequest.getEmail(), mailCheckRequest.getCode());

        if (!isVerified) {
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_INVALID);
        }

        MailCheckResponse data = MailCheckResponse.success(mailCheckRequest.getEmail());
        String message = messageUtil.getMessage(SuccessCode.MAIL_VERIFIED.getMessageKey());

        return ResponseEntity.ok(SuccessResponse.of(message, data));
    }
}

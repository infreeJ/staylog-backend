package com.staylog.staylog.domain.auth.service.impl;

import com.staylog.staylog.domain.auth.dto.EmailVerificationDto;
import com.staylog.staylog.domain.auth.repository.AuthMapper;
import com.staylog.staylog.domain.auth.service.MailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final AuthMapper authMapper;
    private static final String SENDER_EMAIL = "kasious84@gmail.com";

    @Override
    @Transactional
    public void sendVerificationMail(String email) {
        String code = createVerificationCode();

        EmailVerificationDto verificationDto = new EmailVerificationDto();
        verificationDto.setEmail(email);
        verificationDto.setVerificationCode(code);
        verificationDto.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // 5분 뒤 만료
        verificationDto.setIsVerified("N");

        authMapper.saveOrUpdateVerification(verificationDto);

        try {
            MimeMessage message = createMail(email, code);
            javaMailSender.send(message);
        } catch (Exception e) {
            // 실무에서는 Log.error()를 사용해 에러 로깅을 해야 합니다.
            throw new RuntimeException("메일 발송 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Transactional
    public boolean verifyMail(String email, String code) {
        EmailVerificationDto storedVerification = authMapper.findVerificationByEmail(email);

        if (storedVerification == null || !"N".equals(storedVerification.getIsVerified())) {
            return false; // 인증 요청 기록이 없거나 이미 인증된 상태
        }
        if (storedVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false; // 만료된 경우
        }
        if (!storedVerification.getVerificationCode().equals(code)) {
            return false; // 인증 코드가 일치하지 않는 경우
        }

        // 인증 성공
        storedVerification.setIsVerified("Y");
        // isVerified 상태만 'Y'로 업데이트
        authMapper.saveOrUpdateVerification(storedVerification);
        return true;
    }

    private String createVerificationCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    private MimeMessage createMail(String mail, String code) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(SENDER_EMAIL);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("StayLog 이메일 인증");
        String body = "<h3>요청하신 인증 번호입니다.</h3><h1>" + code + "</h1><h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");
        return message;
    }
}
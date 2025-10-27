package com.staylog.staylog.domain.auth.service.impl;

import com.staylog.staylog.domain.auth.dto.EmailVerificationDto;
import com.staylog.staylog.domain.auth.mapper.EmailMapper;
import com.staylog.staylog.domain.auth.service.MailService;
import com.staylog.staylog.domain.user.mapper.UserMapper;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail; // private.properties

    private final EmailMapper emailMapper;
    private final UserMapper userMapper;



    /**
     * 인증 메일 전송 메서드
     * @author 이준혁
     * @param email 인증을 받을 이메일 주소
     * @return 인증 코드 만료 시간
     */
    @Override
    @Transactional
    public LocalDateTime sendVerificationMail(String email) {

        // users 테이블에서 이메일 중복 확인
        if(userMapper.findByEmail(email) != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        EmailVerificationDto verificationDto = emailMapper.findVerificationByEmail(email);

        String code = createVerificationCode(); // 랜덤 코드

        // email 테이블에 입력받은 이메일이 없다면 해당하는 새로 Dto 생성
        if(verificationDto == null) {
            verificationDto = new EmailVerificationDto();
            verificationDto.setEmail(email);
        }
        verificationDto.setVerificationCode(code);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10); // 10분 뒤 만료
        verificationDto.setExpiresAt(expiresAt);
        verificationDto.setIsVerified("N"); // 미인증 상태

        emailMapper.saveOrUpdateVerification(verificationDto);

        try {
            MimeMessage message = createMail(email, code);
            javaMailSender.send(message);
            log.info("인증 메일 발송 완료: {}", email);
            return expiresAt;
        } catch (Exception e) {
            log.error("메일 발송 중 오류 발생: {}", email, e);
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAILED, e);
        }
    }

    /**
     * 인증 코드 검증 메서드
     * @author 이준혁
     * @param email 이메일 주소
     * @param code 사용자가 입력한 인증번호
     * @return
     */
    @Override
    @Transactional
    public boolean verifyMail(String email, String code) {
        EmailVerificationDto storedVerification = emailMapper.findVerificationByEmail(email);

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
        emailMapper.saveOrUpdateVerification(storedVerification);
        return true;
    }

    /**
     * 인증 번호 생성 메서드
     * @author 이준혁
     * @return 인증 번호
     */
    private String createVerificationCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    /**
     * 인증 메일 생성 메서드
     * @author 이준혁
     * @param email
     * @param code
     * @return 인증 메일 내용
     * @throws Exception
     */
    private MimeMessage createMail(String email, String code) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("StayLog 이메일 인증");
        String body = "<h3>요청하신 인증 번호입니다.</h3><h1>" + code + "</h1><h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");
        return message;
    }
}
package com.staylog.staylog.domain.user.dto.request;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원가입에 사용할 Dto
 * @author 이준혁
 */
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private long userId;
    private String userName;
    private String userNickname;
    private String email;
    private String password;
    private String phone;
}

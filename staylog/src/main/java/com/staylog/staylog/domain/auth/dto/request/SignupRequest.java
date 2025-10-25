package com.staylog.staylog.domain.auth.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Alias("signupRequest")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private Long userId;
    private String loginId;
    private String password;
    private String nickname;
    private String name;
    private String email;
    private String phone;
    private String role;
}

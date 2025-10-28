package com.staylog.staylog.domain.auth.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * 회원가입 및 유효성 검증 Dto
 * @author 이준혁
 */
@Alias("signupRequest")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private Long userId;

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^(?![0-9]+$)[a-zA-Z0-9]{4,16}$",
            message = "유효하지 않은 아이디입니다. 영문을 포함하여 4~16글자 입력해주세요")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[\\W_]).{8,}$",
            message = "유효하지 않은 비밀번호입니다. 대문자 + 소문자 + 특수문자 조합으로 8글자 이상 입력해주세요")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^[0-9]+-[0-9]+-[0-9]+$",
            message = "'-'를 포함하여 입력해주세요")
    private String phone;
}

package com.staylog.staylog.domain.user.controller;

import com.staylog.staylog.domain.user.dto.UserDto;
import com.staylog.staylog.domain.user.dto.request.SignupRequest;
import com.staylog.staylog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * 회원가입 메서드
     * @author 이준혁
     * @return 생성된 유저의 PK
     */
    @PostMapping("/user")
    public long UserSignup(SignupRequest signupRequest) {
        return userService.signupUser(signupRequest);
    }





}

package com.staylog.staylog.domain.user.service.impl;


import com.staylog.staylog.domain.user.dto.UserDto;
import com.staylog.staylog.domain.user.dto.request.SignupRequest;
import com.staylog.staylog.domain.user.service.UserService;
import com.staylog.staylog.global.exception.custom.DuplicateSignupException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 메서드
     * @author 이준혁
     * @param signupRequest
     * @return 생성된 유저의 PK
     */
    @Override
    public long signupUser(SignupRequest signupRequest) {

        /*
        //이곳에 데이터 INSERT

        UserDto result = userMapper.getByUserId(signupRequest.getUserId()); // mapper 만들면 사용
        */

        // Swagger 테스트 후에는 result 변수 타입과 return type을 UserDto로 바꿔서 검증 필요
        long result = 1;
        if(result != 1) {
            throw new DuplicateSignupException("이미 사용 중인 아이디입니다.");
        }

        String encoedPassword = passwordEncoder.encode(signupRequest.getPassword());
        signupRequest.setPassword(encoedPassword);

//        return userMapper.signupUser(signupRequest).getUserId(); 테스트 후 사용
        return 1;
    }


}

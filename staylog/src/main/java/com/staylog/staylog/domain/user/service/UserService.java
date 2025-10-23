package com.staylog.staylog.domain.user.service;

import com.staylog.staylog.domain.user.dto.UserDto;
import com.staylog.staylog.domain.user.dto.request.SignupRequest;

public interface UserService {

    public long signupUser(SignupRequest signupRequest);

}

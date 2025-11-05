package com.staylog.staylog.domain.auth.dto.request;

import lombok.*;

@Getter
@Setter
public class LoginRequest {

    String loginId;
    String password;

}

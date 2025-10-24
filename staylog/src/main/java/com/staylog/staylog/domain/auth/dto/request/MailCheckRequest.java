package com.staylog.staylog.domain.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailCheckRequest {
    private String email;
    private String code;
}

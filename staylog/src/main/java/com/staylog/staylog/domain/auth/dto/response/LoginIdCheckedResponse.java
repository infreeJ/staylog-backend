package com.staylog.staylog.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginIdCheckedResponse {
    private String loginId;
    private boolean isDuplicate;
}

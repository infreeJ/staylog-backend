package com.staylog.staylog.external.toss.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TossErrorResponse {
    private String code;       // 토스 에러 코드 (예: INVALID_CARD)
    private String message;    // 토스 에러 메시지
}

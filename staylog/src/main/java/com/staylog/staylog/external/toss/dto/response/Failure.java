package com.staylog.staylog.external.toss.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 토스 결제 실패 정보
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Failure {
    private String code;       // 실패 코드
    private String message;    // 실패 메시지
}

package com.staylog.staylog.external.toss.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 토스 결제 취소 내역
 */
@Getter
@Setter
public class Cancel {
    private Long cancelAmount;
    private String cancelReason;
    private String canceledAt;
    private String transactionKey;
}

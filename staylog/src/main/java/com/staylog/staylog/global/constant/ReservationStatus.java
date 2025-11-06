package com.staylog.staylog.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 예약 상태 Enum
 * COMMON_CODE 테이블의 RESERVATION_STATUS와 매칭
 */
@Getter
@AllArgsConstructor
public enum ReservationStatus {

    PENDING("RES_PENDING", "예약 대기중"),
    CONFIRMED("RES_CONFIRMED", "예약 확정"),
    CANCELED("RES_CANCELED", "예약 취소"),
    REFUNDED("RES_REFUNDED", "환불 완료"),
    EXPIRED("RES_EXPIRED", "예약 만료"),
    COMPLETED("RES_COMPLETED", "이용 완료");

    private final String code;         // COMMON_CODE.CODE_ID
    private final String description;  // 설명

    /**
     * 코드로 Enum 찾기
     */
    public static ReservationStatus fromCode(String code) {
        for (ReservationStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ReservationStatus code: " + code);
    }
}

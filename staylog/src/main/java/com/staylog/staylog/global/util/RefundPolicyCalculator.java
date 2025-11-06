package com.staylog.staylog.global.util;

import com.staylog.staylog.global.constant.RefundType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 환불 정책 계산 유틸리티
 * - 체크인 날짜 기준 환불율 계산
 * - 환불 정책: 7일 전(100%), 5일 전(80%), 3일 전(50%), 1일 전(30%), 당일(0%)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefundPolicyCalculator {

    /**
     * 체크인 날짜 기준 환불 유형 계산
     * @param checkInDate 체크인 날짜
     * @param requestDate 환불 요청 날짜 (오늘)
     * @return RefundType
     */
    public static RefundType calculateRefundType(LocalDate checkInDate, LocalDate requestDate) {
        long daysUntilCheckIn = ChronoUnit.DAYS.between(requestDate, checkInDate);

        if (daysUntilCheckIn >= 7) {
            return RefundType.FULL;      // 7일 이상: 100% 환불
        } else if (daysUntilCheckIn >= 5) {
            return RefundType.EIGHTY;    // 5~6일: 80% 환불
        } else if (daysUntilCheckIn >= 3) {
            return RefundType.FIFTY;     // 3~4일: 50% 환불
        } else if (daysUntilCheckIn >= 1) {
            return RefundType.THIRTY;    // 1~2일: 30% 환불
        } else {
            return RefundType.CUSTOM;    // 당일: 환불 불가 (0%)
        }
    }

    /**
     * 환불 금액 계산
     * @param totalAmount 총 결제 금액
     * @param refundType 환불 유형
     * @return 환불 금액
     */
    public static Long calculateRefundAmount(Long totalAmount, RefundType refundType) {
        if (refundType == RefundType.CUSTOM) {
            return 0L;  // 당일 환불 불가
        }

        return totalAmount * refundType.getPercentage() / 100;
    }

    /**
     * 체크인 날짜 기준 환불 가능 여부 확인
     * @param checkInDate 체크인 날짜
     * @param requestDate 환불 요청 날짜
     * @return 환불 가능 여부
     */
    public static boolean isRefundable(LocalDate checkInDate, LocalDate requestDate) {
        long daysUntilCheckIn = ChronoUnit.DAYS.between(requestDate, checkInDate);
        return daysUntilCheckIn >= 1;  // 최소 1일 전까지 환불 가능
    }

    /**
     * 환불 정책 메시지 반환
     * @param refundType 환불 유형
     * @return 환불 정책 설명 메시지
     */
    public static String getRefundPolicyMessage(RefundType refundType) {
        return switch (refundType) {
            case FULL -> "체크인 7일 전까지 전액 환불됩니다.";
            case EIGHTY -> "체크인 5~6일 전이므로 80% 환불됩니다.";
            case FIFTY -> "체크인 3~4일 전이므로 50% 환불됩니다.";
            case THIRTY -> "체크인 1~2일 전이므로 30% 환불됩니다.";
            case CUSTOM -> "당일 취소는 환불이 불가능합니다.";
        };
    }
}

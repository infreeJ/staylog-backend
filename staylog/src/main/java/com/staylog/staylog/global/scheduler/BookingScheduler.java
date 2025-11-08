package com.staylog.staylog.global.scheduler;

import com.staylog.staylog.domain.booking.service.BookingService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 예약 관련 스케줄러
 * - 만료된 예약 자동 취소 (5분 타임아웃)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingScheduler {

    private final BookingService bookingService;

    /**
     * 만료된 예약 처리 스케줄러
     * - 실행 주기: 1분마다
     * - 대상: PENDING 상태이며 생성 후 5분 이상 경과한 예약
     * - 동작: RES_CANCELED 상태로 변경
     */
    @Scheduled(cron = "0 * * * * *")  // 매분 0초에 실행
    public void handleExpiredBookings() {
        log.debug("만료된 예약 처리 시작");

        try {
            int canceledCount = bookingService.cancelExpiredBookings();

            if (canceledCount > 0) {
                log.info("만료된 예약 {} 건 자동 취소 완료", canceledCount);
            }
        } catch (Exception e) {

            log.error("만료된 예약 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new SchedulingException(ErrorCode.BOOKING_EXPIRED.getCode());
        }
    }
}

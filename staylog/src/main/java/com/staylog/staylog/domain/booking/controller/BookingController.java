package com.staylog.staylog.domain.booking.controller;

import com.staylog.staylog.domain.booking.dto.request.CreateBookingRequest;
import com.staylog.staylog.domain.booking.dto.response.BookingDetailResponse;
import com.staylog.staylog.domain.booking.service.BookingService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import com.staylog.staylog.global.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 예약 API 컨트롤러
 */
@Tag(name = "BookingController", description = "예약 API")
@RestController
@RequestMapping("/v1/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;
    private final MessageUtil messageUtil;

    /**
     * 예약 생성
     * POST /v1/bookings
     *
     * @param user JWT 인증된 사용자 정보 (Spring Security에서 자동 주입)
     * @param request 예약 생성 요청
     * @return 생성된 예약 정보 (bookingId, bookingNum, expiresAt 포함)
     */
    @Operation(summary = "예약 생성", description = "객실 예약을 생성합니다. 5분 이내에 결제를 완료해야 합니다.")
    @PostMapping
    public ResponseEntity<SuccessResponse<BookingDetailResponse>> createBooking(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody CreateBookingRequest request
    ) {
        Long userId = user.getUserId();
        String guestName = user.getNickname();  // JWT에서 닉네임 추출
        log.info("예약 생성 요청: userId={}, guestName={}, roomId={}", userId, guestName, request.getRoomId());

        BookingDetailResponse response = bookingService.createBooking(userId, guestName, request);

        String message = messageUtil.getMessage(SuccessCode.BOOKING_CREATED.getMessageKey());
        String code = SuccessCode.BOOKING_CREATED.name();

        return ResponseEntity.ok(SuccessResponse.of(code, message, response));
    }

    /**
     * 예약 조회
     * GET /v1/bookings/{bookingId}
     *
     * @param bookingId 예약 ID
     * @return 예약 정보
     */
    @Operation(summary = "예약 조회", description = "예약 ID로 예약 정보를 조회합니다.")
    @GetMapping("/{bookingId}")
    public ResponseEntity<SuccessResponse<BookingDetailResponse>> getBooking(
            @PathVariable Long bookingId
    ) {
        log.info("예약 조회 요청: bookingId={}", bookingId);

        BookingDetailResponse response = bookingService.getBooking(bookingId);
        log.info("response : BookingDetailResponse = {}", response);

        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();

        return ResponseEntity.ok(SuccessResponse.of(code, message, response));
    }
}

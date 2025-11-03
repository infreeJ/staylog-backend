package com.staylog.staylog.domain.admin.reservation.controller;

import com.staylog.staylog.domain.admin.reservation.dto.AdminReservationDto;
import com.staylog.staylog.domain.admin.reservation.dto.request.AdminReservationListRequest;
import com.staylog.staylog.domain.admin.reservation.service.AdminReservationService;
import com.staylog.staylog.domain.admin.user.dto.response.AdminGetUserDetailResponse;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 예약 관리 컨트롤러
 * - 예약 목록 조회
 * - 예약 상세 조회
 * - 예약 상태 변경
 * - 예약 전체 건수 조회
 */
@Tag(name = "AdminReservationController", description = "예약 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AdminReservationController {
     private final AdminReservationService reservationService;
     private final MessageUtil messageUtil;

    /**
     * 예약 목록 조회
     */
    @Operation(summary = "모든 예약 목록", description = "모든 예약 목록 조회")
    @GetMapping("/admin/reservations")
    public ResponseEntity<SuccessResponse<List<AdminReservationDto>>> getReservationList (AdminReservationListRequest req) {
        List<AdminReservationDto> list = reservationService.getReservationList(req);
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        SuccessResponse<List<AdminReservationDto>> success = SuccessResponse.of(code,message,list);
        return ResponseEntity.ok(success);
    }
    /**
     * 예약 상세 조회
     */
    @Operation(summary = "예약 상세 조회", description = "예약 정보 조회")
    @GetMapping("/admin/reservations/{bookingId}")
    public ResponseEntity<SuccessResponse<AdminReservationDto>> getReservationDetail(@PathVariable Long bookingId) {
        AdminReservationDto detail = reservationService.getReservaionDetail(bookingId);
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        SuccessResponse<AdminReservationDto> success = SuccessResponse.of(code,message,detail);
        return ResponseEntity.ok(success);
    }
//    /**
//     * 예약 상태 변경
//     */
//    @PatchMapping("/admin/reservations/{bookingId}/status")
//    public ResponseEntity<SuccessResponse<Void>> updateReservationStatus(@PathVariable Long bookingId, @RequestParam("status") String status){
//        boolean update = reservationService.updateReservationStatus(bookingId, status);
//
//    }
}

package com.staylog.staylog.domain.admin.reservation.service.impl;


import com.staylog.staylog.domain.admin.reservation.dto.AdminMonthlyStatsDto;
import com.staylog.staylog.domain.admin.reservation.dto.AdminReservationDto;
import com.staylog.staylog.domain.admin.reservation.dto.request.AdminReservationListRequest;
import com.staylog.staylog.domain.admin.reservation.dto.response.AdminReservationListResponse;
import com.staylog.staylog.domain.admin.reservation.mapper.AdminReservationMapper;
import com.staylog.staylog.domain.admin.reservation.service.AdminReservationService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.common.response.PageResponse;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 관리자 예약 관리 서비스 인터페이스
 *
 * @author 고영석
 */
@Service
@RequiredArgsConstructor
public class AdminReservationServiceImpl implements AdminReservationService {

    private final AdminReservationMapper mapper;


    @Override
    public AdminReservationListResponse getReservationList(AdminReservationListRequest req) {

        int totalCount = mapper.countReservations(req);

        PageResponse page = new PageResponse();
        page.calculate(req, totalCount);

        List<AdminReservationDto> list = mapper.findReservations(req);

        AdminReservationListResponse resp = new AdminReservationListResponse();
        resp.setReservations(list);
        resp.setPage(page);
        return resp; // ✅ 단일 객체 반환
    }

    @Override
    public AdminReservationDto getReservationDetail(Long bookingId)
    {
        return mapper.getReservationDetail(bookingId);
    }

    @Override
    public void updateReservationStatus(Long bookingId, String status) {
        int result = mapper.updateReservationStatus(bookingId, status);
        if (result == 0 ) {
            // 없는 예약이거나 같은 상태로 업데이트 시도 등
            throw new BusinessException(ErrorCode.BOOKING_NOT_FOUND);
        }
    }

    @Override
    public int getTotalCount(AdminReservationListRequest req) {
        return mapper.countReservations(req);
    }

    @Override
    public AdminMonthlyStatsDto getMonthlyStats() {
        AdminMonthlyStatsDto dto = mapper.getMonthlyStats();
        return dto != null ? dto : new AdminMonthlyStatsDto(0, 0, 0, 0L);
    }


}

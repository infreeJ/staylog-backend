package com.staylog.staylog.domain.admin.reservation.service.impl;


import com.staylog.staylog.domain.admin.reservation.dto.AdminReservationDto;
import com.staylog.staylog.domain.admin.reservation.dto.request.AdminReservationListRequest;
import com.staylog.staylog.domain.admin.reservation.mapper.AdminReservationMapper;
import com.staylog.staylog.domain.admin.reservation.service.AdminReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 관리자 예약 관리 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class AdminReservationServiceImpl implements AdminReservationService {

    private final AdminReservationMapper mapper;


    @Override
    public List<AdminReservationDto> getReservationList(AdminReservationListRequest req) {
        return mapper.findReservations(req);
    }

    @Override
    public AdminReservationDto getReservaionDetail(Long bookingId) {
        return mapper.getReservationDetail(bookingId);
    }

    @Override
    public boolean updateReservationStatus(Long bookingId, String status) {
        int result = mapper.updateReservationStatus(bookingId, status);
        return result > 0; // true : 성공 / false : 실패
    }

    @Override
    public int getTotalCount(AdminReservationListRequest req) {
        return mapper.countReservations(req);
    }
}

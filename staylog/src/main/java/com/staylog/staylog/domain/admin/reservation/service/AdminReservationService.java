package com.staylog.staylog.domain.admin.reservation.service;

import com.staylog.staylog.domain.admin.reservation.dto.AdminReservationDto;
import com.staylog.staylog.domain.admin.reservation.dto.request.AdminReservationListRequest;
import com.staylog.staylog.domain.admin.reservation.dto.response.AdminReservationListResponse;

import java.util.List;

/**
 * 관리자 예약 관리 서비스 인터페이스
 * - 예약 목록 조회
 * - 예약 상세 조회
 * - 예약 상태 변경
 */
public interface AdminReservationService {
    /**
     * 예약 목록 조회
     * @param req 검색 조건 및 페이지 정보 Dto
     * @return 예약 목록
     */
    AdminReservationListResponse getReservationList(AdminReservationListRequest req);
    /**
     * 예약 상세 조회
     * @param bookingId 예약 고유 Id
     * @return 예약 상세 Dto
     */
    AdminReservationDto getReservationDetail (Long bookingId);
    /**
     * 예약 상태 변경
     * @param bookingId 예약 고유 Id
     * @param status 변경할 예약 상태 코드 (공통 코드 그룹)
     * @return true : 성공 / false : 실패
     */
    void updateReservationStatus (Long bookingId, String status);
    /**
     * 페이지 네이션을 위한 전체 예약 건수 조회
     *
     * @param req 예약 목록 조회 조건 Dto
     * @return 전체 예약 건수
     */
    int getTotalCount (AdminReservationListRequest req);
}

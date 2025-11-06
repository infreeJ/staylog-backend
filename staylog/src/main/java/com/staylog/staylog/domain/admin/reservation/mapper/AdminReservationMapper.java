package com.staylog.staylog.domain.admin.reservation.mapper;

import com.staylog.staylog.domain.admin.reservation.dto.AdminReservationDto;
import com.staylog.staylog.domain.admin.reservation.dto.request.AdminReservationListRequest;
import com.staylog.staylog.domain.admin.reservation.dto.response.AdminReservationListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 관리자 예약 관리용 MyBatis 매퍼
 */
@Mapper
public interface AdminReservationMapper {
    /**
     * 예약 목록 조회
     * @param req 검색 조건 및 페이지 정보
     * @return 예약목록
     */
    List<AdminReservationDto> findReservations(AdminReservationListRequest req);

    /**
     * 예약 상세조회
     * @param bookingId 예약 고유 Id
     * @return 예약 상세 Dto
     */
    AdminReservationDto getReservationDetail(@Param("bookingId") Long bookingId);

    /**
     * 예약 상태 변경
     * @param bookingId 예약 고유 Id
     * @param status 변경할 예약 상태 코드(공통코드)
     * @return 업데이트 된 행 수 (0이면 변경 실패 또는 존재하지 않는 예약)
     */
    int updateReservationStatus (@Param("bookingId") Long bookingId, @Param("status") String status);
    /**
     * 페이지네이션을 위한 예약 건 수
     * @param req 예약 목록 조회 조건Dto
     * @return 전체 예약 건수
     */
    int countReservations (AdminReservationListRequest req);

}

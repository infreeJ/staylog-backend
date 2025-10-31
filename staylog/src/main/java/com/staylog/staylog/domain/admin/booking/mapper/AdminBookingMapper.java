package com.staylog.staylog.domain.admin.booking.mapper;

import com.staylog.staylog.domain.admin.booking.dto.AdminBookingDto;
import com.staylog.staylog.domain.admin.booking.dto.request.AdminBookingListRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 관리자 예약 관리용 MyBatis 매퍼
 */
@Mapper
public interface AdminBookingMapper {
    /**
     * 예약 목록 조회
     * @param req 검색 조건 및 페이지 정보
     * @return 예약목록
     */
    List<AdminBookingDto> findBookings (AdminBookingListRequest req);

}

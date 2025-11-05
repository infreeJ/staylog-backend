package com.staylog.staylog.domain.admin.accommodation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.staylog.staylog.domain.admin.accommodation.dto.request.AccommodationUpdateStatusRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationSearchRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.response.AdminAccommodationDetailResponse;

@Mapper
public interface AdminAccommodationMapper {

    /**
     * 숙소 목록 조회
     * @param searchRequest 검색 조건 (지역, 숙소타입, 키워드, 삭제여부)
     * @return 숙소 목록
     */
    List<AdminAccommodationDetailResponse> selectAccommodationList(AdminAccommodationSearchRequest searchRequest);

    /**
     * 숙소 상세 조회
     * @param accommodationId 숙소 ID
     * @return 숙소 상세 정보
     */
    AdminAccommodationDetailResponse selectAccommodationDetail(@Param("accommodationId") Long accommodationId);

    /**
     * 숙소 전체 수정
     * @param request 숙소 수정 요청 정보
     * @return 수정된 행 수
     */
    int updateAccommodation(AdminAccommodationRequest request);

    /**
     * 숙소 상태전환 (삭제/복원)
     * @param request 숙소 상태 변경 요청 DTO (accommodationId, deletedYn 포함)
     * @return 삭제된 행 수
     * */
    int updateAccommodationStatus(AccommodationUpdateStatusRequest request);
    
    /**
     * 숙소의 모든 객실 상태전환 (삭제/복원)
     * @param request 객실 상태 변경 요청 DTO (accommodationId, deletedYn 포함)
     * @return 삭제된 행 수
     * */
    int updateAccommodationRoomStatus(AccommodationUpdateStatusRequest request);

    /**
     * 숙소 등록
     * @param request 숙소 등록 요청 정보
     * @return 등록된 행 수
     */
    int insertAccommodation(AdminAccommodationRequest request);
}

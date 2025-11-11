package com.staylog.staylog.domain.accommodation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.staylog.staylog.domain.accommodation.dto.response.AccommodationDetailResponse;
import com.staylog.staylog.domain.accommodation.dto.response.ReviewResponse;
import com.staylog.staylog.domain.accommodation.dto.response.RoomListResponse;

@Mapper
public interface AccommodationMapper {
	/**
	 * 숙소에 대한 상세 내용
	 * @param accommodationId : 조회할 숙소의 고유번호 (PK) 
	 * @return 숙소 상세 내용
	 * */
	AccommodationDetailResponse selectAcDetail(@Param("accommodationId") Long accommodationId);
	
	/**
     * 해당 숙소에 속한 객실 목록 조회
     * @param accommodationId : 조회할 숙소 고유번호
     * @return 객실 목록 리스트
     */
    List<RoomListResponse> selectRoomList(@Param("accommodationId") Long accommodationId);
    
    /**
     * 해당 숙소에 대한 리뷰 목록 조회
     * @param accommodationId : 조회할 숙소의 고유번호
     * @return 리뷰 목록 리스트
     * */
    List<ReviewResponse> selectReviewList(@Param("accommodationId") Long accommodationId);
    
    /**
     * 해당 숙소에 대한 전체 리뷰 수 조회
     * @param accommodationId : 조회할 숙소의 고유번호
     * @return 전체 리뷰 수
     * */
    int selectReviewCount(@Param("accommodationId") Long accommodationId);
}
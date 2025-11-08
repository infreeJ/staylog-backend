package com.staylog.staylog.domain.accommodation.service;

import java.util.List;

import com.staylog.staylog.domain.accommodation.dto.response.AccommodationDetailResponse;
import com.staylog.staylog.domain.accommodation.dto.response.ReviewResponse;

public interface AccommodationService {
	
	/**
     * 숙소의 상세 정보를 조회 (현재는 이미지 없이 기본 정보만 조회)
     * @param accommodationId 조회할 숙소의 기본키
     * @return 숙소 상세 응답 dto
     */
    public AccommodationDetailResponse getAcDetail(Long accommodationId);
    
    /**
     * 해당 숙소의 전체 리뷰 목록 조회
     * 
     * @param accommodationId 조회할 숙소의 기본키
     * @return 숙소 리뷰 리스트
     */
    List<ReviewResponse> getAcRvList(Long accommodationId);
}
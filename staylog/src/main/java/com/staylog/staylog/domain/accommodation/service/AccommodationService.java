package com.staylog.staylog.domain.accommodation.service;

import com.staylog.staylog.domain.accommodation.dto.response.AccommodationDetailResponse;

public interface AccommodationService {
	
	/**
     * 숙소의 상세 정보를 조회 (현재는 이미지 없이 기본 정보만 조회)
     * @param accommodationId 조회할 숙소의 기본키
     * @return 숙소 상세 응답 dto
     */
    public AccommodationDetailResponse getAcDetail(Long accommodationId);
    
    
    
}

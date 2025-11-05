package com.staylog.staylog.domain.admin.accommodation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 숙소 검색 요청 DTO
 * 숙소 목록 조회 시 필터링 조건을 담는 객체
 * 
 * @author 천승현
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAccommodationSearchRequest {
    
	/** 지역 코드 */
    private String regionCode;
    
    /** 숙소 타입 코드 */
    private String acType;
    
    /** 숙소명 검색 키워드 (부분 일치) */
    private String keyword;
    
    /** 삭제 여부 (Y: 삭제됨, N: 활성) */
    private String deletedYn;

}
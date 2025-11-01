package com.staylog.staylog.domain.admin.accommodation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 숙소 응답 DTO
 * 숙소 정보를 조회하여 반환할 때 사용하는 객체
 * 
 * @author 천승현
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAccommodationListResponse {
    
    private Long accommodationId;
    
    private String name;
    
    /** 숙소 타입 ID (FK - common_code) */
    private String acType;
    /** 숙소 타입명 (호텔, 리조트 등) */
    private String typeName;
    
    /** 지역 ID (FK - common_code) */
    private String regionCode;
    /** 지역명 (호텔, 리조트 등) */
    private String regionName;
    
    /** 삭제 여부 (Y: 삭제됨, N: 활성) */
    private String deletedYn;
    
    private String createdAt;
}
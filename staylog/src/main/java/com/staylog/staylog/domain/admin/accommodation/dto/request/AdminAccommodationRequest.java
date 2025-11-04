package com.staylog.staylog.domain.admin.accommodation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 숙소 등록/수정 요청 DTO
 * 숙소를 등록하거나 수정할 때 사용하는 객체
 * 
 * @author StayLog Team
 * @since 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAccommodationRequest {
    
    private Long accommodationId;
    
    private String name;
    
    /** 숙소 타입 코드 */
    private String acType;
    
    /** 지역 코드 */
    private String regionCode;
    
    private String description;
    
    private String address;
    
    /** 위도 (지도 표시용) */
    private Double latitude;
    /** 경도 (지도 표시용) */
    private Double longitude;
    
    private String checkInTime;
    
    private String checkOutTime;
}
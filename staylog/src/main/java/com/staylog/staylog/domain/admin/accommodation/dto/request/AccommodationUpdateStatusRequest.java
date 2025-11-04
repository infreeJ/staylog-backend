package com.staylog.staylog.domain.admin.accommodation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 숙소 상태변환 요청 DTO
 * 숙소를 삭제하거나 복원할 때 사용하는 객체
 * 
 * @author 천승현
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationUpdateStatusRequest {
	
    private Long accommodationId;
    
    /** 변환활 상태값 */
    private String deletedYn;
}

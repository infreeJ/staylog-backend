package com.staylog.staylog.domain.admin.room.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 객실 상태변환 요청 DTO
 * 객실을 삭제하거나 복원할 때 사용하는 객체
 * 
 * @author 천승현
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdateStatusRequest {
	
    private Long roomId;
    
    /** 변환활 상태값 */
    private String deletedYn;
}

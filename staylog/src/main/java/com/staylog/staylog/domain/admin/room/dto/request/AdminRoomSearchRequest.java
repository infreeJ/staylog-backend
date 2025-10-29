package com.staylog.staylog.domain.admin.room.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 객실 검색 요청 DTO
 * 객실 목록 조회 시 필터링 조건을 담는 객체
 *
 * @author 천승현
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoomSearchRequest {

    /** 숙소 ID (필수) */
    private Long accommodationId;

    /** 객실 타입 코드 ID */
    private String rmType;
    
    /** 객실명 검색 키워드 (부분 일치) */
    private String keyword;

    /** 삭제 여부 (Y: 삭제됨, N: 활성) */
    private String deletedYn;
}

package com.staylog.staylog.domain.admin.room.dto.response;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 객실 응답 DTO
 * 객실 정보를 조회하여 반환할 때 사용하는 객체
 *
 * @author 천승현
 */
@Alias("AdminRoomDetailResponse")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoomDetailResponse {

    /** 숙소 ID */
    private Long accommodationId;
    
    /** 숙소 ID */
    private String accommodationName;

    /** 객실 ID */
    private Long roomId;

    /** 객실명 */
    private String name;

    /** 객실 타입 ID (FK - common_code) */
    private String rmType;
    /** 객실 타입명 (스탠다드, 디럭스 등) */
    private String typeName;

    /** 1박 가격 */
    private Integer price;

    /** 최대 성인 수 */
    private Integer maxAdult;
    /** 최대 어린이 수 */
    private Integer maxChildren;
    /** 최대 유아 수 */
    private Integer maxInfant;

    /** 체크인 시간 */
    private String checkIn;
    /** 체크아웃 시간 */
    private String checkOut;

    /** 객실 면적 (m²) */
    private Double area;

    /** 삭제 여부 (Y: 삭제됨, N: 활성) */
    private String deletedYn;

    /** 싱글 베드 수 */
    private Integer singleBed;
    /** 더블 베드 수 */
    private Integer doubleBed;
    /** 퀸 베드 수 */
    private Integer queenBed;
    /** 킹 베드 수 */
    private Integer kingBed;

    /** 생성일시 */
    private String createdAt;
    /** 수정일시 */
    private String updatedAt;
}

package com.staylog.staylog.domain.admin.room.dto.request;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 객실 등록/수정 요청 DTO
 * 객실을 등록하거나 수정할 때 사용하는 객체
 *
 * @author 천승현
 */
@Alias("AdminRoomRequest")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoomRequest {

    /** 객실 ID (수정 시 사용) */
    private Long roomId;

    /** 숙소 ID (필수) */
    private Long accommodationId;

    /** 객실명 (필수) */
    private String name;

    /** 객실 타입 코드 */
    private String rmType;

    /** 1박 가격 (필수) */
    private Integer price;

    /** 최대 성인 수 */
    private Integer maxAdult;
    /** 최대 어린이 수 */
    private Integer maxChildren;
    /** 최대 유아 수 */
    private Integer maxInfant;

    /** 객실 면적 (m²) */
    private Double area;

    /** 싱글 베드 수 */
    private Integer singleBed;
    /** 더블 베드 수 */
    private Integer doubleBed;
    /** 퀸 베드 수 */
    private Integer queenBed;
    /** 킹 베드 수 */
    private Integer kingBed;
    
    /** 객실 설명 */
    private String description;
}
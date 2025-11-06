package com.staylog.staylog.domain.accommodation.dto.response;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Alias("AccommodationDetailResponse")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationDetailResponse {
	private Long accommodationId; // 숙소 번호 (PK)
	private String name; // 숙소명

	// DB 에서 NUMBER 타입의 type 컬럼을 COMMON_CODE 테이블과 조인하여 변환하는 값
	private String typeNameEn; // 숙소 유형명 영어 (code_name_en : ACCOM_HOTEL 등)
	private String typeName; // 숙소 유형명 (code_name : 호텔, 민박 등)z
	
	private String description; // 숙소 상세 설명
	private String address; // 숙소 상세 주소
	
	// DB 에서 NUMBER 타입의 region 컬럼과 COMMON_CODE 테이블과 조인하여 변환하는 값
	private String regionNameEn; // 숙소 지역명 영어 (code_name_en : REGION_SEOUL 등)
	private String regionName; // 숙소 지역명 (code_name : 서울 등)

	// 지도 API 에 사용
    private Double latitude; // 위도
    private Double longitude; // 경도

    private Timestamp createdAt; // 등록일
    private Timestamp updatedAt; // 수정일
    // private String checkInTime; // 관리자가 등록한 숙소 전체 기본 체크인 시간
    // private String checkOutTime; // 관리자가 등록한 숙소 전체 기본 체크아웃 시간
    
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    
    private List<RoomListResponse> rooms; // 객실 목록
    private List<ReviewResponse> reviews;

    // private Double averageRating; // 숙소 평균 별졈
    // private List<이미지 관련 Response dto 클래스명> images; 이미지 목록
}

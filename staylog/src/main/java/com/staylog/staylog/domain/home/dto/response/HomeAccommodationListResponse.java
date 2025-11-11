package com.staylog.staylog.domain.home.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomeAccommodationListResponse {
	private Long accommodationId;
	private String name;
	private String regionCode;
	private String acType;
	
	private LocalDateTime createdAt;
	
	private Double ratingAvg; //평균 별점
	private Integer reviewCnt; //리뷰 개수
	
	private Integer minPrice; //최소 가격
	
	private String address;
	
	private String regionName; // 숙소 지역명 (code_name : 서울 등)

	
}

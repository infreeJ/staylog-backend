package com.staylog.staylog.domain.accommodation.dto.request;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("accommodationCreateRequest")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationCreateRequest {

	private Long accommodationId;
	private String name;
	private String acType; //공통코드
	private String description;
	private String address;
	private String region;  //공통코드
    private Double latitude;  // 카카오 지오코딩으로 채움
    private Double longitude; // 카카오 지오코딩으로 채움
    private String createdAt;
	private String updatedAt;  
	private String checkInTime;
	private String checkOutTime;
	private String deletedYN;
   
}

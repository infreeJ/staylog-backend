package com.staylog.staylog.domain.accommodation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomDetailResponse {
	
	private long roomId;
	private long accommodationId;
	private String name;
	private String type;
	private int price;
	private int maxAdult;
	private int maxChildren;
	private int maxInfant;
	private String checkIn;
	private String checkOut;
	private String status;
	private int area;
	private int singleBed;
	private int doubleBed;
	private int queenBed;
	private int kingBed;
	
	private String typeCode;
	private String typeName;

	//체크인, 체크아웃 시간만
    private String checkInTime;
    private String checkOutTime;

    private String description;
	

}

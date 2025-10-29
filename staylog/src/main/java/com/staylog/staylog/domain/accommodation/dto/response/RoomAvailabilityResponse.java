package com.staylog.staylog.domain.accommodation.dto.response;

import java.util.List;
//예약가능한 날짜 리스트형식
public class RoomAvailabilityResponse {

	private long roomId;//객실 ID
	private List<String> blockedDates; //예약 가능한 날짜
}

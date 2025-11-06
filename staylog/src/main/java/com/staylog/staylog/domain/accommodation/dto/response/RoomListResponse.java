package com.staylog.staylog.domain.accommodation.dto.response;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Alias("RoomListResponse")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomListResponse {
	private Long roomId;
	private String name;
	private int price;
	private int maxGuest;
	// 객실 유형 code_id
	private String rmTypeCodeId;
	// 예약 불가일
	private List<String> disabledDates;
}

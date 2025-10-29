package com.staylog.staylog.domain.accommodation.dto.response;

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
	private String rmTypeNameEn;
	private String rmTypeName;
}

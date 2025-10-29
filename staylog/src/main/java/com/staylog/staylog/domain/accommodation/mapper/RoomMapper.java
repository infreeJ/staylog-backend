package com.staylog.staylog.domain.accommodation.mapper;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.staylog.staylog.domain.accommodation.dto.response.RoomDetailResponse;

@Mapper
public interface RoomMapper {
	RoomDetailResponse selectRoomDetailById(@Param("roomId") Long roomId);
	
	List<String> SelectBlockedDates(
			@Param("roomId") Long roomId, 
			@Param("fromDate") Date fromDate,
		    @Param("toDate") Date toDate);
}

package com.staylog.staylog.domain.accommodation.mapper;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.staylog.staylog.domain.accommodation.dto.response.RoomDetailResponse;

@Mapper
public interface RoomMapper {
	
	/* 정나영
	 * 객실 상세 내용
	 * @Param roomId : 조회할 객실의 상세번호
	 * @return : 객실 상세 내용
	 * */
	RoomDetailResponse selectRoomDetailById(@Param("roomId") Long roomId);
	
	

	/* 정나영
	 * 객실 예약 가능 날짜 조회
	 * @Param roomId : 조회할 객실의 상세번호
	 * @Param fromDate : 예약 테이블에서 조회할 날짜의 시작일
	 * @Param toDate : 예약 테이블에서 조회할 날짜의 마지막일
	 * @return : 객실 상세 내용
	 * */
	List<String> SelectBlockedDates(
			@Param("roomId") Long roomId, 
			@Param("fromDate") LocalDate from,
		    @Param("toDate") LocalDate to);
	

	/* 정나영
	 * 객실 존재 여부 조회
	 * @Param roomId : 조회할 객실의 상세번호
	 * @return : 1 또는 1이상 -> 객실 존재 , 0 -> 객실 존재x
	 * */
	int existsRoom(@Param("roomId") Long roomId);
	
	
}

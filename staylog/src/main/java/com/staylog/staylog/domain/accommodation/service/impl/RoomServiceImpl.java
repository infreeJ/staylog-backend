package com.staylog.staylog.domain.accommodation.service.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.accommodation.dto.response.RoomDetailResponse;
import com.staylog.staylog.domain.accommodation.mapper.RoomMapper;
import com.staylog.staylog.domain.accommodation.service.RoomService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

	private final RoomMapper roomMapper;


	
	@Override
	public RoomDetailResponse roomDetail(long roomId) {
		
		RoomDetailResponse dto = roomMapper.selectRoomDetailById(roomId);
		
		if(dto == null) {
			log.warn("객실 불러올 수 없음");
			throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
		}
		
		return dto;
	}

	@Override
	public List<String> blockedDate(Long roomId, Date fromDate, Date toDate) {
		
		// 1) roomId 검증, 요청값 자체 검증
		if(roomId == null || roomId< 0) {
			log.warn("객실 불러올 수 없음");
			throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
		}
		
		//날짜 정규화 -> 자정을 기준으로 잘라서 java.sql.Date 타입으로 변환
        final Date from = Date.valueOf(fromDate.toLocalDate());
        final Date to   = Date.valueOf(toDate.toLocalDate());
		
		//2)날짜 필수
		if(from == null || to == null) {
			log.warn("날짜가 비워져있습니다.");
			throw new BusinessException(ErrorCode.DATE_NOT_FOUND);
		}
		
		//3) 순서 검증
		if(to.before(from)) {
			log.warn("날짜순서가 잘못되었습니다.");
			throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
		}
		
		// 4) Room 존재 검증 
		if (roomMapper.existsRoom(roomId) == 0) {
			log.warn("존재하지 않는 객실입니다. ");
		    throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
		}
		
		try {
			return roomMapper.SelectBlockedDates(roomId, fromDate, toDate);
		}catch(DataAccessException e) {
			log.warn("DB 예외");
			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		
		
	}

	
}

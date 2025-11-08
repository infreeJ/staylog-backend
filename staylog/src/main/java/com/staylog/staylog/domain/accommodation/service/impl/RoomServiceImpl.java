package com.staylog.staylog.domain.accommodation.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
	public List<String> blockedDate(Long roomId, LocalDate from, LocalDate to) {
		
		final long MAX_DAYS = 90L;

		
		// roomId 검증, 요청값 자체 검증
		if(roomId == null || roomId<= 0) {
			log.warn("객실 불러올 수 없음");
			throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
		}
		

		//날짜 필수
		if(from == null || to == null) {
			log.warn("날짜가 비워져있습니다.");
			throw new BusinessException(ErrorCode.DATE_NOT_FOUND);
		}
		 if (to.isBefore(from)) {
	            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
	      }
		 
		//최대 조회 범위 예약 가능일/불가능일 조회는 90일 이내만 가능
		long days = ChronoUnit.DAYS.between(from, to) + 1;
		
	    if (days < 1) {
	        log.warn("INVALID_DATE_RANGE(empty): roomId={}, from={}, to={}", roomId, from, to);
	        throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
	    }
	    if (days > MAX_DAYS) {
	        log.warn("날짜 범위가 너무 깁니다. ");
	        throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
	    }
	    
		
		//Room 존재 검증 
		if (roomMapper.existsRoom(roomId) == 0) {
			log.warn("존재하지 않는 객실입니다. ");
		    throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
		}
		
		
		//조회
		try {
			return roomMapper.SelectBlockedDates(roomId, from, to);
		}catch(DataAccessException e) {
			log.warn("DB 예외");
			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		
		
	}

	
}

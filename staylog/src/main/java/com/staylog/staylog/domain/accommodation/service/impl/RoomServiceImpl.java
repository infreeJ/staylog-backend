package com.staylog.staylog.domain.accommodation.service.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.staylog.staylog.domain.accommodation.dto.response.RoomDetailResponse;
import com.staylog.staylog.domain.accommodation.mapper.RoomMapper;
import com.staylog.staylog.domain.accommodation.service.RoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

	private final RoomMapper roomMapper;
	
	@Override
	public RoomDetailResponse roomDetail(long roomId) {
		
		RoomDetailResponse dto = roomMapper.selectRoomDetailById(roomId);
		
		if(dto == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 객실이 존재하지 않습니다.");
		}
		
		return dto;
	}

	@Override
	public List<String> blockedDate(Long roomId, Date fromDate, Date toDate) {
		
		return roomMapper.SelectBlockedDates(roomId, fromDate, toDate);
	}

	
}

package com.staylog.staylog.domain.accommodation.service;

import java.time.LocalDate;
import java.util.List;

import com.staylog.staylog.domain.accommodation.dto.response.RoomDetailResponse;

public interface RoomService {

	RoomDetailResponse roomDetail(long roomId);
	List<String> blockedDate(Long roomId, LocalDate from, LocalDate to);
	
}

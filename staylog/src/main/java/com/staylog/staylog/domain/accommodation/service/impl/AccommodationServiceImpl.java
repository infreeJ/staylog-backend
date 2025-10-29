package com.staylog.staylog.domain.accommodation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.accommodation.dto.response.AccommodationDetailResponse;
import com.staylog.staylog.domain.accommodation.dto.response.RoomListResponse;
import com.staylog.staylog.domain.accommodation.mapper.AccommodationMapper;
import com.staylog.staylog.domain.accommodation.service.AccommodationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
	
	// 의존 주입
    private final AccommodationMapper mapper;
	
    @Override
	public AccommodationDetailResponse getAcDetail(Long id) {
		// 숙소의 기본 정보 조회 (이미지 제외)
        AccommodationDetailResponse res = mapper.selectAcDetail(id);
        
        
        // TODO : 객실(rooms), 이미지(images) 목록 추가 로직 추가 필요
        // TODO : 리뷰 관련 필드 추가 로직 추가 필요

        if (res == null) {
            // 숙소가 없을 경우 null 반환 또는 예외 처리
            return null; 
        }
        
        // 숙소에 속한 객실 목록 조회
        List<RoomListResponse> roomList = mapper.selectRoomList(id);
        res.setRooms(roomList);
        
        
        return res;
	}
}
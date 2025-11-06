package com.staylog.staylog.domain.admin.room.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomRequest;
import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomSearchRequest;
import com.staylog.staylog.domain.admin.room.dto.request.RoomUpdateStatusRequest;
import com.staylog.staylog.domain.admin.room.dto.response.AdminRoomDetailResponse;
import com.staylog.staylog.domain.admin.room.mapper.AdminRoomMapper;
import com.staylog.staylog.domain.admin.room.service.AdminRoomService;
import com.staylog.staylog.global.common.response.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 객실 서비스 구현체
 * 객실 관련 비즈니스 로직을 처리하고 데이터 접근 계층과 통신합니다.
 *
 * @author 천승현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRoomServiceImpl implements AdminRoomService {

	private final AdminRoomMapper mapper;

	/**
	 * 특정 숙소의 객실 목록 조회
	 * 
	 * @param searchRequest 검색 조건 (숙소ID, 객실타입, 객실명, 삭제여부)
	 * @return 검색 조건에 맞는 객실 목록
	 */
	@Override
	public Map<String, Object> getRoomList(AdminRoomSearchRequest searchRequest) {

		//객실 목록 조회
		List<AdminRoomDetailResponse> rooms = mapper.selectRoomListByAccommodation(searchRequest);
		
        //전체 객실 개수 조회
        int totalCount = mapper.countRoomList(searchRequest);
        
        //페이지 정보 계산
        PageResponse pageResponse = new PageResponse();
        pageResponse.calculate(searchRequest, totalCount);
        
        //결과를 Map 에 담아 반환
        Map<String, Object> result = new HashMap<>();
        result.put("rooms", rooms);
        result.put("page", pageResponse);
        
        log.info("객실 목록 조회 완료 - 총 {}건, 현재 페이지: {}/{}", 
                 totalCount, searchRequest.getPageNum(), pageResponse.getTotalPage());
        
        return result;
	}

	/**
	 * 객실 상세 정보 조회
	 * 
	 * @param roomId 조회할 객실 ID
	 * @return 객실 상세 정보
	 */
	@Override
	public AdminRoomDetailResponse getRoomDetail(Long roomId) {
		return mapper.selectRoomDetail(roomId);
	}

    /**
     * 객실 상태 전환(삭제/복원)
     * 요청 DTO의 deletedYn 값에 따라 객실의 상태를 'Y' (삭제) 또는 'N' (복원)으로 변경합니다.
     * 
     * @param request 상태를 변경할 객실 ID와 변경할 상태(deletedYn)를 포함하는 요청 DTO
     */
	@Override
	public void updateRoomStatus(RoomUpdateStatusRequest request) {
		
		mapper.updateRoomStatus(request);
	}

	/**
	 * 객실 정보 수정
	 * 
	 * @param request 수정할 객실 정보 (roomId, accommodationId 포함)
	 */
	@Override
	public void updateRoom(AdminRoomRequest request) {
		mapper.updateRoom(request);
	}

	/**
	 * 객실 등록
	 * 새로운 객실을 특정 숙소에 등록합니다.
	 * 
	 * @param request 등록할 객실 정보 (accommodationId 필수)
	 */
	@Override
	public void addRoom(AdminRoomRequest request) {
		mapper.insertRoom(request);
	}
}
package com.staylog.staylog.domain.admin.room.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomRequest;
import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomSearchRequest;
import com.staylog.staylog.domain.admin.room.dto.response.AdminRoomDetailResponse;
import com.staylog.staylog.domain.admin.room.mapper.AdminRoomMapper;
import com.staylog.staylog.domain.admin.room.service.AdminRoomService;

import lombok.RequiredArgsConstructor;

/**
 * 관리자 객실 서비스 구현체
 * 객실 관련 비즈니스 로직을 처리하고 데이터 접근 계층과 통신합니다.
 *
 * @author 천승현
 */
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
	public List<AdminRoomDetailResponse> getRoomList(AdminRoomSearchRequest searchRequest) {
		return mapper.selectRoomListByAccommodation(searchRequest);
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
	 * 객실 논리 삭제
	 * deleted_yn을 'Y'로 변경하여 논리적으로 삭제 처리합니다.
	 * 
	 * @param roomId 삭제할 객실 ID
	 */
	@Override
	public void deleteRoom(Long roomId) {
		mapper.deleteRoom(roomId);
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
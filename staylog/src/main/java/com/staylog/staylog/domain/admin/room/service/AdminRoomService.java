package com.staylog.staylog.domain.admin.room.service;

import java.util.List;
import java.util.Map;

import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomRequest;
import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomSearchRequest;
import com.staylog.staylog.domain.admin.room.dto.request.RoomUpdateStatusRequest;
import com.staylog.staylog.domain.admin.room.dto.response.AdminRoomDetailResponse;

/**
 * 관리자 객실 서비스 인터페이스
 * 객실의 등록, 수정, 삭제/복원, 조회 비즈니스 로직을 처리합니다.
 *
 * @author 천승현
 */
public interface AdminRoomService {
    
    /**
     * 특정 숙소의 객실 목록 조회
     * 
     * @param searchRequest 검색 조건 (숙소ID, 객실타입, 객실명, 삭제여부)
     * @return 검색 조건에 맞는 객실 목록
     */
	Map<String, Object> getRoomList(AdminRoomSearchRequest searchRequest);
    
    /**
     * 객실 상세 정보 조회
     * 
     * @param roomId 조회할 객실 ID
     * @return 객실 상세 정보
     */
    AdminRoomDetailResponse getRoomDetail(Long roomId);
    
    /**
     * 객실 상태 전환(삭제/복원)
     * 요청 DTO의 deletedYn 값에 따라 객실의 상태를 'Y' (삭제) 또는 'N' (복원)으로 변경합니다.
     * 
     * @param request 상태를 변경할 객실 ID와 변경할 상태(deletedYn)를 포함하는 요청 DTO
     */
    void updateRoomStatus(RoomUpdateStatusRequest request);
    
    /**
     * 객실 정보 수정
     * 
     * @param request 수정할 객실 정보 (roomId, accommodationId 포함)
     */
    void updateRoom(AdminRoomRequest request);
    
    /**
     * 객실 등록
     * 새로운 객실을 특정 숙소에 등록합니다.
     * roomId는 자동 생성됩니다.
     * 
     * @param request 등록할 객실 정보 (accommodationId 필수)
     */
    void addRoom(AdminRoomRequest request);
}
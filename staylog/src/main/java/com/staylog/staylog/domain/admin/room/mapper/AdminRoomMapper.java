package com.staylog.staylog.domain.admin.room.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomRequest;
import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomSearchRequest;
import com.staylog.staylog.domain.admin.room.dto.response.AdminRoomDetailResponse;

import java.util.List;

@Mapper
public interface AdminRoomMapper {

    /**
     * 숙소별 객실 목록 조회
     * @param searchRequest 검색 조건 (숙소ID, 객실타입, 객실명, 삭제여부)
     * @return 객실 목록
     */
    List<AdminRoomDetailResponse> selectRoomListByAccommodation(AdminRoomSearchRequest searchRequest);

    /**
     * 객실 상세 조회
     * @param roomId 객실 ID
     * @return 객실 상세 정보
     */
    AdminRoomDetailResponse selectRoomDetail(@Param("roomId") Long roomId);

    /**
     * 객실 논리 삭제
     * @param roomId 객실 ID
     * @return 삭제된 행 수
     */
    int deleteRoom(@Param("roomId") Long roomId);
    
    /**
     * 객실 논리 복원
     * @param roomId 객실 ID
     * @return 복원된 행 수
     */
    int restoreRoom(@Param("roomId") Long roomId);

    /**
     * 객실 수정
     * @param request 객실 수정 요청 정보
     * @return 수정된 행 수
     */
    int updateRoom(AdminRoomRequest request);


    /**
     * 객실 추가
     * @param request 객실 등록 요청 정보
     * @return 등록된 행 수
     */
    int insertRoom(AdminRoomRequest request);
}

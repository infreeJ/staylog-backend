package com.staylog.staylog.domain.admin.room.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomRequest;
import com.staylog.staylog.domain.admin.room.dto.request.AdminRoomSearchRequest;
import com.staylog.staylog.domain.admin.room.dto.response.AdminRoomDetailResponse;
import com.staylog.staylog.domain.admin.room.service.AdminRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 관리자 객실 관리 컨트롤러
 * 객실 등록, 수정, 삭제, 조회 기능을 제공합니다.
 *
 * @author 천승현
 */
@Tag(name = "AdminRoomController", description = "관리자 객실 관리 API")
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AdminRoomController {

	private final AdminRoomService roomService;

	/**
	 * 특정 숙소의 객실 목록 조회 (검색 필터 포함)
	 * 
	 * @param accommodationId 숙소 ID (필수)
	 * @param searchRequest 검색 조건 (객실타입, 객실명, 삭제여부 등)
	 * @return 객실 목록
	 */
	@Operation(
		summary = "객실 목록 조회", 
		description = "특정 숙소에 속한 객실 목록을 조회합니다."
	)
	@GetMapping("/admin/accommodations/{accommodationId}/rooms")
	public ResponseEntity<List<AdminRoomDetailResponse>> list(
			@Parameter(description = "숙소 ID") 
			@PathVariable Long accommodationId,
			@Parameter(description = "객실 검색 조건") 
			AdminRoomSearchRequest searchRequest) {
		// accommodationId를 searchRequest에 설정
		searchRequest.setAccommodationId(accommodationId);
		List<AdminRoomDetailResponse> list = roomService.getRoomList(searchRequest);
		return ResponseEntity.ok(list);
	}

	/**
	 * 객실 상세 조회
	 * 
	 * @param accommodationId 숙소 ID
	 * @param roomId 객실 ID
	 * @return 객실 상세 정보
	 */
	@Operation(
		summary = "객실 상세 조회", 
		description = "특정 숙소의 특정 객실 상세 정보를 조회합니다."
	)
	@GetMapping("/admin/accommodations/{accommodationId}/rooms/{roomId}")
	public ResponseEntity<AdminRoomDetailResponse> detail(
			@Parameter(description = "숙소 ID") 
			@PathVariable Long accommodationId,
			@Parameter(description = "객실 ID") 
			@PathVariable Long roomId) {
		AdminRoomDetailResponse response = roomService.getRoomDetail(roomId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 객실 논리 삭제 (상태 전환)
	 * deleted_yn을 'Y'로 변경하여 논리적으로 삭제 처리합니다.
	 * 
	 * @param accommodationId 숙소 ID
	 * @param roomId 삭제할 객실 ID
	 */
	@Operation(
		summary = "객실 삭제", 
		description = "객실을 논리 삭제합니다. (deleted_yn = 'Y')"
	)
	@PatchMapping("/admin/accommodations/{accommodationId}/rooms/{roomId}/delete")
	public ResponseEntity<Void> deleteRoom(
			@Parameter(description = "숙소 ID") 
			@PathVariable Long accommodationId,
			@Parameter(description = "삭제할 객실 ID") 
			@PathVariable Long roomId) {
		roomService.deleteRoom(roomId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 객실 정보 수정
	 * 
	 * @param accommodationId 숙소 ID
	 * @param roomId 수정할 객실 ID
	 * @param request 수정할 객실 정보
	 */
	@Operation(
		summary = "객실 정보 수정", 
		description = "기존 객실의 정보를 수정합니다."
	)
	@PatchMapping("/admin/accommodations/{accommodationId}/rooms/{roomId}")
	public ResponseEntity<Void> updateRoom(
			@Parameter(description = "숙소 ID") 
			@PathVariable Long accommodationId,
			@Parameter(description = "수정할 객실 ID") 
			@PathVariable Long roomId,
			@Parameter(description = "수정할 객실 정보") 
			@RequestBody AdminRoomRequest request) {
		// roomId와 accommodationId를 request에 설정
		request.setRoomId(roomId);
		request.setAccommodationId(accommodationId);
		roomService.updateRoom(request);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 객실 등록
	 * 특정 숙소에 새로운 객실을 등록합니다.
	 * roomId는 자동 생성되므로 요청 시 포함하지 않아도 됩니다.
	 * 
	 * @param accommodationId 숙소 ID
	 * @param request 등록할 객실 정보
	 */
	@Operation(
		summary = "객실 등록", 
		description = "특정 숙소에 새로운 객실을 등록합니다. ID는 자동 생성됩니다."
	)
	@PostMapping("/admin/accommodations/{accommodationId}/rooms")
	public ResponseEntity<Void> addRoom(
			@Parameter(description = "숙소 ID") 
			@PathVariable Long accommodationId,
			@Parameter(description = "등록할 객실 정보") 
			@RequestBody AdminRoomRequest request) {
		// accommodationId를 request에 설정
		request.setAccommodationId(accommodationId);
		roomService.addRoom(request);
		return ResponseEntity.status(201).build();
	}
}
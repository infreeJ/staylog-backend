package com.staylog.staylog.domain.accommodation.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.accommodation.dto.response.RoomDetailResponse;
import com.staylog.staylog.domain.accommodation.service.RoomService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class RoomController {

	private final RoomService roomService;
	private final MessageUtil messageUtil;
	
	@GetMapping("/room/{roomId}")
	public ResponseEntity<SuccessResponse<RoomDetailResponse>> detailRoom(@PathVariable long roomId) {
		
		log.info("객실 상세 요청",roomId);
		
		RoomDetailResponse roomDetailResponse = roomService.roomDetail(roomId);
		String message = messageUtil.getMessage(SuccessCode.ROOM_SUCCESS.getMessageKey());
		String code = SuccessCode.ROOM_SUCCESS.name();
		SuccessResponse<RoomDetailResponse> success = SuccessResponse.of(code, message, roomDetailResponse);
		
		return ResponseEntity.ok(success);
	}
		
	@GetMapping("/{roomId}/blocked")
	public ResponseEntity<SuccessResponse<List<String>>> getBlockedDates(
	        @PathVariable Long roomId,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

		log.info("객실 block 날짜 요청");
		
		List<String> roomAvailable = roomService.blockedDate(
		        roomId, Date.valueOf(from), Date.valueOf(to));
		String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
		String code = SuccessCode.SUCCESS.name();
		SuccessResponse<List<String>> success = SuccessResponse.of(code, message, roomAvailable);
		
		return ResponseEntity.ok(success);


	}
	
}

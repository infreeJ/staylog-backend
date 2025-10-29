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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class RoomController {

	private final RoomService roomService;
	
	@GetMapping("/room/{roomId}")
	public RoomDetailResponse detailRoom(@PathVariable long roomId) {
		return roomService.roomDetail(roomId);
	}
		
	@GetMapping("/{roomId}/blocked")
	public ResponseEntity<List<String>> getBlockedDates(
	        @PathVariable Long roomId,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

	    List<String> blockedDates = roomService.blockedDate(
	        roomId, Date.valueOf(from), Date.valueOf(to));

	    return ResponseEntity.ok(blockedDates);
	}
	
}

package com.staylog.staylog.domain.accommodation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.accommodation.dto.response.AccommodationDetailResponse;
import com.staylog.staylog.domain.accommodation.service.AccommodationService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class AccommodationController {

	// 의존 객체 생성자 주입
    private final AccommodationService service;
    
    @GetMapping("/accommodations/{accommodationId}")
    public ResponseEntity<AccommodationDetailResponse> getAccommodationDetail(@PathVariable Long accommodationId) {
        AccommodationDetailResponse res = service.getAcDetail(accommodationId);
        return ResponseEntity.ok(res);
    }
    
    
    
    
    // TODO: 객실 목록, 리뷰 목록 등이 추가될 예정
}


package com.staylog.staylog.domain.accommodation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.accommodation.dto.request.AccommodationCreateRequest;
import com.staylog.staylog.domain.accommodation.dto.response.AccommodationDetailResponse;
import com.staylog.staylog.domain.accommodation.service.AccommodationCreateService;
import com.staylog.staylog.domain.accommodation.service.AccommodationService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class AccommodationController {

	// 의존 객체 생성자 주입
    private final AccommodationService service;
	private final MessageUtil messageUtil;
	private final AccommodationCreateService accommodationCreateService;
    
    @GetMapping("/accommodations/{accommodationId}")
    public ResponseEntity<AccommodationDetailResponse> getAccommodationDetail(@PathVariable Long accommodationId) {
        AccommodationDetailResponse res = service.getAcDetail(accommodationId);
        return ResponseEntity.ok(res);
    }
    
    
    
    
    // TODO: 객실 목록, 리뷰 목록 등이 추가될 예정
    
    
    
    /*
     * 정나영
     * 숙소 등록
     * @param req : 등록할 숙소 정보 (JSON)
     * @return 생성된 숙소 ID
     */
    @PostMapping("/accommodation")
    public ResponseEntity<SuccessResponse<Long>> createAccommodation (
    		@RequestBody AccommodationCreateRequest req ){
		
    	log.info("숙소등록요청 : {}", req);
    	
    	Long accommodationId = accommodationCreateService.accommodationCreate(req);
    	
    	String message = messageUtil.getMessage(SuccessCode.ACCOMMODATION_CREATED.getMessageKey());
    	String code = SuccessCode.ACCOMMODATION_CREATED.name();
    	SuccessResponse<Long> success = SuccessResponse.of(code, message, accommodationId);
    	
    	return ResponseEntity.ok(success);
    	
    }
    
}











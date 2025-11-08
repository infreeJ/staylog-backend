package com.staylog.staylog.domain.accommodation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.accommodation.dto.response.AccommodationDetailResponse;
import com.staylog.staylog.domain.accommodation.dto.response.ReviewResponse;
import com.staylog.staylog.domain.accommodation.service.AccommodationService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "AccommodationController", description = "숙소 상세 API")
@Slf4j
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class AccommodationController {

	// 의존 객체 생성자 주입
    private final AccommodationService acService;
    private final MessageUtil messageUtil;

    /**
     * 숙소 상세 정보 조회 컨트롤러
     * @author 김채린
     * @param accommodationId 숙소의 ID
     * @return 숙소의 상세 정보
     * */
    @GetMapping("/accommodations/{accommodationId}")
    public ResponseEntity<SuccessResponse<AccommodationDetailResponse>> getAccommodationDetail(@PathVariable Long accommodationId) {
        AccommodationDetailResponse data = acService.getAcDetail(accommodationId);
        String msg = messageUtil.getMessage(SuccessCode.ACCOMMODATION_FOUND.getMessageKey());
        String code = SuccessCode.ACCOMMODATION_FOUND.name();
        return ResponseEntity.ok(SuccessResponse.of(code, msg, data));
    }
    
    /**
     * 숙소 리뷰 전체 조회 컨트롤러
     * @author 김채린
     * @param accommodationId 숙소의 ID
     * @return 해당 숙소의 전체 리뷰 목록
     */ 
    @GetMapping("/accommodations/{accommodationId}/reviews")
    public ResponseEntity<SuccessResponse<List<ReviewResponse>>> getAccommodationReviews(
            @PathVariable Long accommodationId) {
        List<ReviewResponse> reviews = acService.getAcRvList(accommodationId);
        String msg = messageUtil.getMessage(SuccessCode.ACCOMMODATION_REVIEW_LIST_FOUND.getMessageKey());
        String code = SuccessCode.ACCOMMODATION_REVIEW_LIST_FOUND.name();
        return ResponseEntity.ok(SuccessResponse.of(code, msg, reviews));
    }
        
    // TODO: 객실 목록, 리뷰 목록 등이 추가될 예정
}

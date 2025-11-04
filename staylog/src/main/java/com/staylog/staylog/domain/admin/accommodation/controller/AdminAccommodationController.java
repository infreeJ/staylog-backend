package com.staylog.staylog.domain.admin.accommodation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.admin.accommodation.dto.request.AccommodationUpdateStatusRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationSearchRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.response.AdminAccommodationDetailResponse;
import com.staylog.staylog.domain.admin.accommodation.service.AdminAccommodationService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 숙소 관리 컨트롤러
 * 숙소 등록, 수정, 삭제, 복원, 조회 기능을 제공합니다.
 *
 * @author 천승현
 */
@Slf4j
@Tag(name = "AdminAccommodationController", description = "관리자 숙소 관리 API")
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class AdminAccommodationController {
    
    private final AdminAccommodationService accomService;
    private final MessageUtil messageUtil;

    /**
     * 숙소 목록 조회 (검색 필터 포함)
     * 
     * @param searchRequest 검색 조건 (지역, 숙소타입, 키워드, 삭제여부)
     * @return 숙소 목록
     */
    @Operation(
        summary = "숙소 목록 조회", 
        description = "검색 조건에 맞는 숙소 목록을 조회합니다. 모든 파라미터는 선택사항입니다."
    )
    @GetMapping("/admin/accommodations")
    public ResponseEntity<SuccessResponse<List<AdminAccommodationDetailResponse>>> list(
            @Parameter(description = "숙소 검색 조건") AdminAccommodationSearchRequest searchRequest) {
        List<AdminAccommodationDetailResponse> list = accomService.getList(searchRequest);
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        SuccessResponse<List<AdminAccommodationDetailResponse>> success = SuccessResponse.of(code, message, list);
        return ResponseEntity.ok(success);
    }

    /**
     * 숙소 상세 조회
     * 
     * @param accommodationId 숙소 ID
     * @return 숙소 상세 정보
     */
    @Operation(
        summary = "숙소 상세 조회", 
        description = "특정 숙소의 상세 정보를 조회합니다."
    )
    @GetMapping("/admin/accommodations/{accommodationId}")
    public ResponseEntity<SuccessResponse<AdminAccommodationDetailResponse>> detail(
            @Parameter(description = "숙소 ID") 
            @PathVariable Long accommodationId) {
        AdminAccommodationDetailResponse response = accomService.getAccommodation(accommodationId);
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        SuccessResponse<AdminAccommodationDetailResponse> success = SuccessResponse.of(code, message, response);
        return ResponseEntity.ok(success);
    }

    /**
     * 숙소 논리적 삭제 및 복원 (상태 전환)
     * 요청 DTO의 deletedYn 값에 따라 숙소의 상태를 'Y' (삭제) 또는 'N' (복원)으로 변경합니다.
     * 
     * @param accommodationId 상태를 변경할 숙소 ID (URL 경로에서 추출)
     * @param request 변경할 상태 값 (deletedYn = 'Y' 또는 'N')
     */
    @Operation(
		summary = "숙소 상태 전환 (삭제/복원)", 
        description = "숙소의 논리적 삭제 여부(deleted_yn)를 'Y' 또는 'N'으로 변경합니다."
    )
    @PatchMapping("/admin/accommodations/{accommodationId}/status")
    public ResponseEntity<SuccessResponse<Void>> updateAccommodationStatus(
    		@Parameter(description = "상태를 변경할 숙소 ID") 
            @PathVariable Long accommodationId, // @PathVariable은 ID를 받도록 수정
            @RequestBody AccommodationUpdateStatusRequest request) {
    	request.setAccommodationId(accommodationId);
        accomService.updateAccommodationStatus(request);
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }

    /**
     * 숙소 정보 수정
     * 
     * @param accommodationId 수정할 숙소 ID
     * @param request 수정할 숙소 정보
     */
    @Operation(
        summary = "숙소 정보 수정", 
        description = "기존 숙소의 정보를 수정합니다."
    )
    @PatchMapping("/admin/accommodations/{accommodationId}")
    public ResponseEntity<SuccessResponse<Void>> updateAccommodation(
            @Parameter(description = "수정할 숙소 ID") 
            @PathVariable Long accommodationId,
            @Parameter(description = "수정할 숙소 정보") 
            @RequestBody AdminAccommodationRequest request) {
        // accommodationId를 request 에 설정
    	request.setAccommodationId(accommodationId);
        accomService.updateAccommodation(request);
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }

    /**
     * 숙소 등록
     * 새로운 숙소를 시스템에 등록합니다.
     * accommodationId는 자동 생성되므로 요청 시 포함하지 않아도 됩니다.
     * 
     * @param request 등록할 숙소 정보
     */
    @Operation(
        summary = "숙소 등록", 
        description = "새로운 숙소를 등록합니다. ID는 자동 생성됩니다."
    )
    @PostMapping("/admin/accommodations")
    public ResponseEntity<SuccessResponse<Void>> addAccommodation(
            @Parameter(description = "등록할 숙소 정보") 
            @RequestBody AdminAccommodationRequest request) {
    	
    	log.info("숙소등록요청 : {}", request);
        accomService.addAccommodation(request);
        String message = messageUtil.getMessage(SuccessCode.ACCOMMODATION_CREATED.getMessageKey());
        String code = SuccessCode.ACCOMMODATION_CREATED.name();
        return ResponseEntity.ok(SuccessResponse.of(code, message, null));
    }
}
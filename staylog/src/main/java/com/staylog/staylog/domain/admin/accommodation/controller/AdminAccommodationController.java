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

import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationSearchRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.response.AdminAccommodationDetailResponse;
import com.staylog.staylog.domain.admin.accommodation.service.AdminAccommodationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 관리자 숙소 관리 컨트롤러
 * 숙소 등록, 수정, 삭제, 조회 기능을 제공합니다.
 *
 * @author 천승현
 */
@Tag(name = "AdminAccommodationController", description = "관리자 숙소 관리 API")
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class AdminAccommodationController {
    
    private final AdminAccommodationService accomService;

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
    public ResponseEntity<List<AdminAccommodationDetailResponse>> list(
            @Parameter(description = "숙소 검색 조건") AdminAccommodationSearchRequest searchRequest) {
        List<AdminAccommodationDetailResponse> list = accomService.getList(searchRequest);
        return ResponseEntity.ok(list);
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
    public ResponseEntity<AdminAccommodationDetailResponse> detail(
            @Parameter(description = "숙소 ID") 
            @PathVariable Long accommodationId) {
        AdminAccommodationDetailResponse response = accomService.getAccommodation(accommodationId);
        return ResponseEntity.ok(response);
    }

    /**
     * 숙소 논리 삭제 (상태 전환)
     * deleted_yn을 'Y'로 변경하여 논리적으로 삭제 처리합니다.
     * 
     * @param accommodationId 삭제할 숙소 ID
     */
    @Operation(
        summary = "숙소 삭제", 
        description = "숙소를 논리 삭제합니다. (deleted_yn = 'Y')"
    )
    @PatchMapping("/admin/accommodations/{accommodationId}/delete")
    public ResponseEntity<Void> deleteAccommodation(
            @Parameter(description = "삭제할 숙소 ID") 
            @PathVariable Long accommodationId) {
        accomService.deleteAccommodation(accommodationId);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Void> updateAccommodation(
            @Parameter(description = "수정할 숙소 ID") 
            @PathVariable Long accommodationId,
            @Parameter(description = "수정할 숙소 정보") 
            @RequestBody AdminAccommodationRequest request) {
        // accommodationId를 request 에 설정
    	request.setAccommodationId(accommodationId);
        accomService.updateAccommodation(request);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Void> addAccommodation(
            @Parameter(description = "등록할 숙소 정보") 
            @RequestBody AdminAccommodationRequest request) {
        accomService.addAccommodation(request);
        return ResponseEntity.status(201).build();
    }
}
package com.staylog.staylog.domain.admin.accommodation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationSearchRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.response.AdminAccommodationDetailResponse;
import com.staylog.staylog.domain.admin.accommodation.mapper.AdminAccommodationMapper;
import com.staylog.staylog.domain.admin.accommodation.service.AdminAccommodationService;

import lombok.RequiredArgsConstructor;


/**
 * 관리자 숙소 관리 서비스 구현체
 * 숙소의 등록, 수정, 삭제, 조회 기능을 제공합니다.
 *
 * @author 천승현
 */
@Service
@RequiredArgsConstructor
public class AdminAccommodationServiceImpl implements AdminAccommodationService {
	
	private final AdminAccommodationMapper mapper;
	
	/**
	 * 숙소 목록 조회
	 * 검색 조건에 따라 필터링된 숙소 목록을 반환합니다.
	 * 
	 * @param searchRequest 검색 조건 (지역, 숙소타입, 키워드, 삭제여부)
	 * @return 검색 조건에 맞는 숙소 목록
	 */
	@Override
	public List<AdminAccommodationDetailResponse> getList(AdminAccommodationSearchRequest searchRequest) {
		
		return mapper.selectAccommodationList(searchRequest);
	}
	
	/**
	 * 숙소 상세 조회
	 * 특정 숙소의 상세 정보를 조회합니다.
	 * 
	 * @param accommodationId 조회할 숙소 ID
	 * @return 숙소 상세 정보
	 */
	@Override
	public AdminAccommodationDetailResponse getAccommodation(Long accommodationId) {
		
		return mapper.selectAccommodationDetail(accommodationId);
	}
	
	/**
	 * 숙소 정보 수정
	 * 기존 숙소의 정보를 수정합니다.
	 * 
	 * @param request 수정할 숙소 정보 (accommodationId 포함 필수)
	 */
	@Override
	public void updateAccommodation(AdminAccommodationRequest request) {
		
		mapper.updateAccommodation(request);
		
	}
	
	/**
	 * 숙소 삭제
	 * 숙소를 삭제 상태로 변경합니다. (deleted_yn = 'Y')
	 * 
	 * @param accommodationId 삭제할 숙소 ID
	 */
	@Override
	public void deleteAccommodation(Long accommodationId) {
		
		mapper.deleteAccommodation(accommodationId);
	}
	
	/**
	 * 숙소 복원
	 * 숙소를 복원 상태로 변경합니다. (deleted_yn = 'N')
	 * 
	 * @param accommodationId 복원할 숙소 ID
	 */
	@Override
	public void restoreAccommodation(Long accommodationId) {
		
		mapper.restoreAccommodation(accommodationId);
	}
	
	/**
	 * 숙소 등록
	 * 새로운 숙소를 등록합니다.
	 * 
	 * @param request 등록할 숙소 정보
	 */
	@Override
	public void addAccommodation(AdminAccommodationRequest request) {
		
		mapper.insertAccommodation(request);
	}
}

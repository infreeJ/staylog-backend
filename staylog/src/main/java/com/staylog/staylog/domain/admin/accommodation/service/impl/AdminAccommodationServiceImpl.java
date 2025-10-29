package com.staylog.staylog.domain.admin.accommodation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationSearchRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.response.AdminAccommodationDetailResponse;
import com.staylog.staylog.domain.admin.accommodation.mapper.AdminAccommodationMapper;
import com.staylog.staylog.domain.admin.accommodation.service.AdminAccommodationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAccommodationServiceImpl implements AdminAccommodationService {
	
	private final AdminAccommodationMapper mapper;
	
	@Override
	public List<AdminAccommodationDetailResponse> getList(AdminAccommodationSearchRequest searchRequest) {
		
		return mapper.selectAccommodationList(searchRequest);
	}
	
	@Override
	public AdminAccommodationDetailResponse getAccommodation(Long accommodationId) {
		
		return mapper.selectAccommodationDetail(accommodationId);
	}
	
	@Override
	public void updateAccommodation(AdminAccommodationRequest request) {
		
		mapper.updateAccommodation(request);
		
	}
	
	@Override
	public void deleteAccommodation(Long accommodationId) {
		
		mapper.deleteAccommodation(accommodationId);
	}
	
	@Override
	public void addAccommodation(AdminAccommodationRequest request) {
		
		mapper.insertAccommodation(request);
	}
}

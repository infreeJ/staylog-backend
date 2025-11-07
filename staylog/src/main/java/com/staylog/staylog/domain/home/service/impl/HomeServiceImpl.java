package com.staylog.staylog.domain.home.service.impl;


import com.staylog.staylog.domain.accommodation.mapper.RoomMapper;
import com.staylog.staylog.domain.accommodation.service.impl.RoomServiceImpl;
import com.staylog.staylog.domain.home.dto.request.HomeAccommodationListRequest;
import com.staylog.staylog.domain.home.dto.response.HomeAccommodationListResponse;
import com.staylog.staylog.domain.home.mapper.HomeMapper;
import com.staylog.staylog.domain.home.service.HomeService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

	private final HomeMapper homeMapper;
	
	//정나영 : 정렬가능한 값만 허용하겠다. / Set : 중복을 허용하지 않는 배역
	private static final Set<String> ALLOWED_SORT = Set.of("rating", "review", "price", "latest");

	
	@Override
	public List<HomeAccommodationListResponse> homeAccommodationList(HomeAccommodationListRequest dto) {
		
		//기본갑 검증
		//0부터 시작, 음수 들어오면 0으로
		int offset = Math.max(dto.getOffset(),0);
		//limit이 0이거나 음수이면 기본 20으로 		
		int limit = dto.getLimit() > 0 ? dto.getLimit() : 20;
		
		String sort = dto.getSort();
		
		String regionCode = dto.getRegionCode();
		
		if(!ALLOWED_SORT.contains(sort)) {
			sort = null;
		}
 				
		return homeMapper.selectAccommodationMain(regionCode, sort, offset, limit);
	}
}











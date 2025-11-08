package com.staylog.staylog.domain.home.service;

import java.util.List;

import com.staylog.staylog.domain.home.dto.request.HomeAccommodationListRequest;
import com.staylog.staylog.domain.home.dto.response.HomeAccommodationListResponse;

public interface HomeService {
	
	List<HomeAccommodationListResponse> homeAccommodationList(HomeAccommodationListRequest req);
}

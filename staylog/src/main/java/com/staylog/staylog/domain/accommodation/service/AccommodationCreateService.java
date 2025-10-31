package com.staylog.staylog.domain.accommodation.service;

import com.staylog.staylog.domain.accommodation.dto.request.AccommodationCreateRequest;

public interface AccommodationCreateService {

	long accommodationCreate(AccommodationCreateRequest req);
}

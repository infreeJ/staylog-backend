package com.staylog.staylog.domain.search.service;

import com.staylog.staylog.domain.search.dto.request.AccomListRequest;
import com.staylog.staylog.domain.search.dto.response.AccomListResponse;

import java.util.List;

public interface SearchService {

    /**
     * - 예약 가능한 숙소 ID 추출
     * -  숙소 기본 정보 + 객실 집계
     * - 예약 수 조회 (popular 정렬 시만 실행하는 쿼리)
     * @param request 검색 조건
     * @return 숙소 리스트
     */
    List<AccomListResponse> searchAccommodations(AccomListRequest request);

}

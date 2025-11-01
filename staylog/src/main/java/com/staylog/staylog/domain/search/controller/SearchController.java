package com.staylog.staylog.domain.search.controller;

import com.staylog.staylog.domain.auth.dto.response.LoginResponse;
import com.staylog.staylog.domain.search.dto.request.AccomListRequest;
import com.staylog.staylog.domain.search.dto.response.AccomListResponse;
import com.staylog.staylog.domain.search.mapper.SearchMapper;
import com.staylog.staylog.domain.search.service.SearchService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "AuthController", description = "인증/인가 API")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class SearchController {

    private final SearchMapper searchMapper;
    private final MessageUtil messageUtil;

    @GetMapping("/search/accommodations")
    public ResponseEntity<SuccessResponse<List<AccomListResponse>>> searchAccommodations(
            @ModelAttribute AccomListRequest request) {
        
        List<AccomListResponse> accomListResponse = searchMapper.getAccomList(request);

        log.info("조건에 따른 예약 가능한 객실을 가진 숙소 리스트 조회 개수: {}", accomListResponse.size());

        // 개별 숙소 정보 상세 로그
        for (AccomListResponse accom : accomListResponse) {
            log.info("숙소 ID: {}", accom.getAccommodationId());
            log.info("이름: {}",  accom.getAccommodationName());
            log.info("지역: {}", accom.getRegionName());
            log.info("최대 인원 수 : {}", accom.getMaxCapacity());
            log.info("최저가: {}", accom.getBasePrice());
            log.info("예약 수 (인기순 정렬용): {}", accom.getReservationCount());
        }
        
        String code = SuccessCode.SUCCESS.name();
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        SuccessResponse<List<AccomListResponse>> success = SuccessResponse.of(code, message, accomListResponse);

        return ResponseEntity.ok(success);


    }

}

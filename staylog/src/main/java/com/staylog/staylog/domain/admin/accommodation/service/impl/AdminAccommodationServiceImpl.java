package com.staylog.staylog.domain.admin.accommodation.service.impl;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.staylog.staylog.domain.admin.accommodation.dto.request.AccommodationUpdateStatusRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.request.AdminAccommodationSearchRequest;
import com.staylog.staylog.domain.admin.accommodation.dto.response.AdminAccommodationDetailResponse;
import com.staylog.staylog.domain.admin.accommodation.dto.response.KakaoResponse;
import com.staylog.staylog.domain.admin.accommodation.mapper.AdminAccommodationMapper;
import com.staylog.staylog.domain.admin.accommodation.service.AdminAccommodationService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.common.response.PageResponse;
import com.staylog.staylog.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 관리자 숙소 관리 서비스 구현체
 * 숙소의 등록, 수정, 삭제/복원, 조회 기능을 제공합니다.
 *
 * @author 천승현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAccommodationServiceImpl implements AdminAccommodationService {
	
	private final AdminAccommodationMapper mapper;
	private final WebClient kakaoWebClient;  //카카오 api 호출용
	
	/**
	 * 숙소 목록 조회
	 * 검색 조건에 따라 필터링된 숙소 목록을 반환합니다.
	 * 
	 * @param searchRequest 검색 조건 (지역, 숙소타입, 키워드, 삭제여부)
	 * @return 검색 조건에 맞는 숙소 목록
	 */
	@Override
	public Map<String, Object> getList(AdminAccommodationSearchRequest searchRequest) {
		
		//숙소 목록 조회
		List<AdminAccommodationDetailResponse> accommodations = mapper.selectAccommodationList(searchRequest);
		
        //전체 숙소 개수 조회
        int totalCount = mapper.countAccommodationList(searchRequest);
        
        //페이지 정보 계산
        PageResponse pageResponse = new PageResponse();
        pageResponse.calculate(searchRequest, totalCount);
        
        //결과를 Map 에 담아 반환
        Map<String, Object> result = new HashMap<>();
        result.put("accommodations", accommodations);
        result.put("page", pageResponse);
        
        log.info("숙소 목록 조회 완료 - 총 {}건, 현재 페이지: {}/{}", 
                 totalCount, searchRequest.getPageNum(), pageResponse.getTotalPage());
        
        return result;
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
	 * 숙소 상태변환 (삭제/복원)
	 * 요청 DTO의 deletedYn 값에 따라 숙소의 상태를 'Y' (삭제) 또는 'N' (복원)으로 변경합니다.
	 * * @param request 상태를 변경할 숙소 ID와 변경할 상태(deletedYn)를 포함하는 요청 DTO
	 */
	@Override
	public void updateAccommodationStatus(AccommodationUpdateStatusRequest request) {
		
		mapper.updateAccommodationStatus(request);
		mapper.updateAccommodationRoomStatus(request); // 숙소의 객실 상태값 처리
	}
	
	/**
	 * 숙소 등록
	 * 새로운 숙소를 등록합니다.
	 * 
	 * @param request 등록할 숙소 정보
	 */
	@Override
	public void addAccommodation(AdminAccommodationRequest request) {
		
		//검증
		if(request.getAddress().isBlank()) {
			throw new BusinessException(ErrorCode.ADDR_NOT_FOUND);
		}
		
		LatLng latLng = geocode(request.getAddress());
		request.setLatitude(latLng.lat);
		request.setLongitude(latLng.lng);
		
		int rows = mapper.insertAccommodation(request);
		if(rows != 1 || request.getAccommodationId() == null) {
			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		
		log.info("숙소 등록 완료 id={}, lat={}, lng={}", request.getAccommodationId(),request.getLatitude(), request.getLongitude());
		
	}
	
	//카카오 api 호출 : 주소 -> 좌표 변환
	//WebClient는 스프링이 제공하는 “HTTP 요청 도구
	//카카오지도 서버에 get 요청을 보내고 json응답을 자바 객체로 변환
	private LatLng geocode(String address) {
        try {
            KakaoResponse res = kakaoWebClient.get() //요청 시작
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", address)
                            .build())
                    .retrieve() //응답을 받는다. 
                    .onStatus(HttpStatusCode::is4xxClientError, r -> r.bodyToMono(String.class) //오류처리
                            .map(body -> {
                                log.warn("Kakao 4xx Error: {}", body);
                                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "카카오 요청 오류");
                            }))
                    .onStatus(HttpStatusCode::is5xxServerError, r -> r.bodyToMono(String.class)
                            .map(body -> {
                                log.error("Kakao 5xx Error: {}", body);
                                return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "카카오 서버 오류");
                            }))
                    .bodyToMono(KakaoResponse.class) //응답 JSON → 자바 객체로 변환
                    .block(Duration.ofSeconds(5)); // 최대 5초 대기 -> 비동기이지만 동기처럼 결과를 바로 받아옴
            

            // 응답 검증
            if (res == null || res.getDocuments() == null || res.getDocuments().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주소로 좌표를 찾지 못했습니다.");
            }

            //결과
            var doc = res.getDocuments().get(0); //첫번쨰 검색 결과
            double lat = Double.parseDouble(doc.getY()); //위도
            double lng = Double.parseDouble(doc.getX()); //경도
            return new LatLng(lat, lng); //객체에 담아 반환

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("카카오 지오코딩 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "카카오 주소검색 실패");
        }
    }
	
	
    // 내부 좌표 클래스 -> 임시 객체
    private static class LatLng {
        final Double lat;
        final Double lng;
        LatLng(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }
}

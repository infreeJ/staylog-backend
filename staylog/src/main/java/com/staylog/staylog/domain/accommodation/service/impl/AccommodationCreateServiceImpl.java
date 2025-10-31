package com.staylog.staylog.domain.accommodation.service.impl;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.staylog.staylog.domain.accommodation.dto.request.AccommodationCreateRequest;
import com.staylog.staylog.domain.accommodation.dto.response.KakaoResponse;
import com.staylog.staylog.domain.accommodation.mapper.AccommodationMapper;
import com.staylog.staylog.domain.accommodation.service.AccommodationCreateService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationCreateServiceImpl implements AccommodationCreateService{

	private final AccommodationMapper accommodationMapper;
	private final WebClient kakaoWebClient;  //카카오 api 호출용
	

	@Override
	public long accommodationCreate(AccommodationCreateRequest req) {
		
		//검증
		if(req.getAddress().isBlank()) {
			throw new BusinessException(ErrorCode.ADDR_NOT_FOUND);
		}
		
		LatLng latLng = geocode(req.getAddress());
		req.setLatitude(latLng.lat);
		req.setLongitude(latLng.lng);
		
		int rows = accommodationMapper.insertAccommodation(req);
		if(rows != 1 || req.getAccommodationId() == null) {
			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		
		log.info("숙소 등록 완료 id={}, lat={}, lng={}", req.getAccommodationId(),req.getLatitude(), req.getLongitude());
		
		return req.getAccommodationId();
		
		
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

















package com.staylog.staylog.domain.accommodation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.accommodation.dto.response.AccommodationDetailResponse;
import com.staylog.staylog.domain.accommodation.dto.response.ReviewResponse;
import com.staylog.staylog.domain.accommodation.dto.response.RoomListResponse;
import com.staylog.staylog.domain.accommodation.mapper.AccommodationMapper;
import com.staylog.staylog.domain.accommodation.mapper.RoomMapper;
import com.staylog.staylog.domain.accommodation.service.AccommodationService;
import com.staylog.staylog.domain.image.assembler.ImageAssembler;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccommodationServiceImpl implements AccommodationService {
	
	// 의존 주입
    private final AccommodationMapper acMapper;
    private final RoomMapper rmMapper;
    private final ImageAssembler imageAssembler;
	
    @Override
	public AccommodationDetailResponse getAcDetail(Long accommodationId) {
    		log.info("숙소 상세 정보 조회 시작 - accommodationId={}", accommodationId);
		// 숙소의 기본 정보 조회 (이미지 제외)
        AccommodationDetailResponse accommodation = acMapper.selectAcDetail(accommodationId);

        if (accommodation == null) {
            log.warn("숙소 조회 실패 : 숙소 번호 = {}의 숙소를 찾을 수 없습니다 - accommodationId={}", accommodationId);
            throw new BusinessException(ErrorCode.ACCOMMODATION_NOT_FOUND);
        }
        
        // 해당 숙소에 대한 객실 목록 조회
        List<RoomListResponse> roomList = acMapper.selectRoomList(accommodationId);
        
        if (roomList == null) {
            log.warn("객실 목록 조회 실패 : 숙소 번호 = {} 의 객실 목록을 찾을 수 없습니다", accommodationId);
            throw new BusinessException(ErrorCode.ROOM_LIST_NOT_FOUND);
        }        
        
        accommodation.setRooms(roomList);
        
        // 해당 숙소에 대한 리뷰 목록 조회
        List<ReviewResponse> reviewList = acMapper.selectReviewList(accommodationId);
        
        // 오류 발생 시
        if (reviewList == null) {
            log.error("리뷰 목록 조회 실패 : 숙소 번호 = {} 의 리뷰 목록을 찾을 수 없습니다", accommodationId);
            throw new BusinessException(ErrorCode.ACCOMMODATION_REVIEW_LIST_NOT_FOUND);
        }
        // 실제로 해당 숙소의 리뷰가 없을 경우 (오류 X)
        if (reviewList.isEmpty()) {
            log.info("숙소 번호 = {} 의 리뷰는 존재하지 않습니다", accommodationId);
        }
        
        imageAssembler.assembleMainImageUrl(reviewList, ReviewResponse::getUserId, ReviewResponse::setProfileUrl, "PROFILE");
        
        accommodation.setReviews(reviewList);
        
        return accommodation;
	}

	@Override
	public List<ReviewResponse> getAcRvList(Long accommodationId) {
		List<ReviewResponse> reviewList = acMapper.selectReviewList(accommodationId);
		
		if(reviewList == null) {
			throw new BusinessException(ErrorCode.ACCOMMODATION_REVIEW_LIST_NOT_FOUND);
	    }
		
		imageAssembler.assembleMainImageUrl(reviewList, ReviewResponse::getUserId, ReviewResponse::setProfileUrl, "PROFILE");
		imageAssembler.assembleMainImageUrl(reviewList, ReviewResponse::getBoardId, ReviewResponse::setContentUrl, "BOARD_REVIEW_CONTENT");
	    
	    return reviewList;
	}
}
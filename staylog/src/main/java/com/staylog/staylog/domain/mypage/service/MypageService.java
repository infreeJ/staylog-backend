package com.staylog.staylog.domain.mypage.service;

import java.util.List;

import com.staylog.staylog.domain.mypage.dto.MemberInfoDto;
import com.staylog.staylog.domain.mypage.dto.response.BookingInfoResponse;
import com.staylog.staylog.domain.mypage.dto.response.ReviewInfoResponse;


/**
 * 마이페이지 서비스 인터페이스 (마이페이지 관련 기능의 비즈니스 로직 정의)
 * @author 오미나
 */
public interface MypageService {
	
	/** 회원정보 조회 
     * @param userId 조회할 회원의 고유번호 (PK)
     * @return 회원정보 DTO (MemberInfoDto)
     * @throws 회원정보가 없을 경우 USER_NOT_FOUND 예외 발생
	 */
    MemberInfoDto getMemberInfo(Long userId);
    
    
    /**
     * 회원 정보 수정
     * 프론트엔드에서 전달된 UpdateMemberInfoRequest DTO를 이용해 회원정보 수정
     * @param req 수정할 회원정보를 담은 UpdateMemberInfoRequest DTO
     * @throws 수정할 회원이 존재하지 않을 경우 USER_NOT_FOUND 예외 발생
     */
    void updateMemberInfo(MemberInfoDto dto);
    
    /** 회원정보 삭제
     * @param userId 탈퇴 처리할 회원의 고유 번호
     * @throws 회원정보가 없을 경우 USER_NOT_FOUND 예외 발생 
     */
//    void deleteMemberInfo(Long userId);
    
    
    /**
     * 예약정보 조회 (해당 회원의 예약 목록을 status 조건으로 조회)
     * @param userId 예약 내역을 조회할 회원의 고유 번호
     * @param status 예약 상태 (upcoming / completed / canceled)
     * @return 조건에 맞는 예약 내역 리스트 (BookingInfoResponse)
     */
    List<BookingInfoResponse> getBookings(Long userId, String status);
    
    
    /**
     * 예약 상세 조회 (모달 창)
     * @param userId 현재 로그인한 회원의 ID
     * @param bookingId 조회할 예약의 ID
     * @return 예약 상세 정보 DTO
     */
    BookingInfoResponse getBookingDetail(Long userId, Long bookingId);
    
    
    /**
     * 리뷰내역 조회(type 값에 따라 분류하여 조회)
     * @param userId 리뷰 내역을 조회할 회원의 고유 번호
     * @param type 리뷰 구분 타입 (writable / written)
     * @return 조건에 맞는 리뷰 내역 리스트 (ReviewInfoResponse)
     */
    List<ReviewInfoResponse> getReviews(Long userId, String type);
    
    /**
     * 문의내역 조회
     * @param userId 문의 내역을 조회할 회원의 고유 번호
     * @param type 문의 구분 타입 (faq / my / waiting)
     * @return 조건에 맞는 문의 내역 리스트 (InquiryInfoResponse)
     */
//    List<InquiryInfoResponse> getInquiries(Long userId, String type);
    
    /**
     * 1:1 문의 작성 (회원이 작성한 문의 내용을 DB에 저장)
     * @param req 문의 제목, 내용, 작성자 정보를 담은 InquiryWriteRequest DTO
     */
//    void writeInquiry(InquiryWriteRequest req);

}


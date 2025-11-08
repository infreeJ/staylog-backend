package com.staylog.staylog.domain.mypage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.staylog.staylog.domain.mypage.dto.MemberInfoDto;
import com.staylog.staylog.domain.mypage.dto.response.BookingInfoResponse;
import com.staylog.staylog.domain.mypage.dto.response.ReviewInfoResponse;

/**
 * 마이페이지 관련 쿼리들을 모아놓은 인터페이스
 * @Author 오미나
 */
@Mapper
public interface MypageMapper {
    /**
     * 회원 한명의 정보를 userId로 조회
     * @param userId 회원 고유번호
     * @return 회원의 상세 정보를 담은 MemberInfoResponse DTO
     */
    MemberInfoDto selectMemberInfo(Long userId);
    
    
    /**
     * 회원정보 수정(update)
     * @param req 수정할 회원정보를 담은 DTO
     * @return 수정된 행(row)의 개수 (성공 시 1, 실패시 0)
     */
    int updateMemberInfo(MemberInfoDto dto);
    
    
	 /**
	  * 회원정보 삭제 (탈퇴 미정)
	  * @param userId 삭제할 회원의 고유 번호
	  * @return 삭제된 행(row)의 개수 (성공 시 1)
	  */
    // int deleteMemberInfo(Long userId);

    
    /** 예약목록 조회
     * @param userId 예약 내역을 조회할 회원의 고유 번호
     * @param status 예약 상태 필터 값
     * @return 조건에 맞는 예약 내역 리스트
     */
    List<BookingInfoResponse> selectBookings(@Param("userId") Long userId, @Param("status") String status);

    
     /**
      * 예약 상세 조회
      * @param userId 현재 로그인한 회원의 ID
      * @param bookingId 조회할 예약의 ID
      * @return 예약 상세 정보 DTO
     */
     BookingInfoResponse selectBookingDetail(@Param("userId") Long userId, @Param("bookingId") Long bookingId);    
    
     
    /** 리뷰목록 조회
     * @param userId 리뷰를 조회할 회원의 고유 번호
     * @param type 리뷰 구분 타입
     * @return 조건에 맞는 리뷰 내역 리스트
     */
    List<ReviewInfoResponse> selectReviews(@Param("userId") Long userId, @Param("type") String type);
    
    
    /** 문의목록 조회 (문의 미정)
     * @param userId 문의 내역을 조회할 회원의 고유 번호
     * @param type 문의 구분 타입
     * @return 조건에 맞는 문의 내역 리스트
     */		
    // List<InquiryInfoResponse> selectInquiries(@Param("userId") Long userId, @Param("type") String type);
    
    
    /** 회원이 작성한 1:1 문의 DB에 저
     * @param req 문의 제목, 내용, 작성자 정보가 담긴 InquiryWriteRequest DTO
     * @return 삽입된 행(row)의 개수 (성공 시 1)
     */
    // int insertInquiry(InquiryWriteRequest req);
}


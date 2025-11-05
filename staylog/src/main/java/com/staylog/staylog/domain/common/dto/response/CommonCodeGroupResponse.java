package com.staylog.staylog.domain.common.dto.response;

import com.staylog.staylog.domain.common.dto.CommonCodeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 그룹별 공통코드 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonCodeGroupResponse {
    private List<CommonCodeDto> regions;              // 지역 코드
    private List<CommonCodeDto> accommodationTypes;   // 숙소 타입
    private List<CommonCodeDto> roomTypes;            // 객실 타입
    private List<CommonCodeDto> roomStatus;           // 객실 상태
    private List<CommonCodeDto> reservationStatus;    // 예약 상태
    private List<CommonCodeDto> paymentStatus;        // 결제 상태
    private List<CommonCodeDto> paymentMethods;       // 결제 수단
    private List<CommonCodeDto> refundStatus;         // 환불 상태
    private List<CommonCodeDto> refundTypes;          // 환불 유형
    private List<CommonCodeDto> notificationTypes;    // 알림 유형
    private List<CommonCodeDto> boardTypes;           // 게시판 유형
    private List<CommonCodeDto> imageTypes;           // 이미지 타입
    private List<CommonCodeDto> userRoles;            // 사용자 권한
    private List<CommonCodeDto> userStatus;           // 사용자 상태
    private List<CommonCodeDto> order;                // 정렬 타입
}

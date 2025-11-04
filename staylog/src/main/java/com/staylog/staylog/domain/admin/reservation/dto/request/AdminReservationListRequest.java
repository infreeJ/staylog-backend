package com.staylog.staylog.domain.admin.reservation.dto.request;

import com.staylog.staylog.global.common.dto.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관리자 예약 목록 조회 요청 Dto
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminReservationListRequest extends PageRequest {
    private String status; // 예약 상태
    private String guestName; // 투숙객
    private String userName; // 회원 이름
    private Long userId; // 회원 Id
    private Long stayId; // 숙소 Id
    private String  statusGroupId; // 상태 코드 그룹
    private String startDate; // 조회 시작일
    private String endDate; // 조회 종료일
}

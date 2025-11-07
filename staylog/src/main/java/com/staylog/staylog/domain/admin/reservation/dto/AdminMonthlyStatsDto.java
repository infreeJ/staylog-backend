package com.staylog.staylog.domain.admin.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 이번 달 매출 요약을 위한 Dto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMonthlyStatsDto {
    private int totalCount;
    private int confirmedCount;
    private int canceledCount;
    private long monthlyRevenue;


}

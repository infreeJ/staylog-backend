package com.staylog.staylog.domain.coupon.dto.request;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

/**
 * 전체 사용자 쿠폰 발급에 사용하는 Dto
 * @author 이준혁
 */
@Alias("couponBatchRequest")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponBatchRequest {
    private String name;
    private int discount;
    private LocalDate expiredAt;
}

package com.staylog.staylog.domain.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Alias("couponResponse")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {
    private long couponId;
    private long userId;
    private String name;
    private int discount;
    private String isUsed;
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;
}

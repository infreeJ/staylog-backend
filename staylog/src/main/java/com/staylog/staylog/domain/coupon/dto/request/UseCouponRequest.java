package com.staylog.staylog.domain.coupon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Alias("useCouponRequest")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UseCouponRequest {
    private long couponId;
}

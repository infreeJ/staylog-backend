package com.staylog.staylog.domain.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Alias("couponCheckDto")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponCheckDto {
    private String isUsed;
    private LocalDateTime expiredAt;
}

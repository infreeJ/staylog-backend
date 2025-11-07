package com.staylog.staylog.global.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponCreatedEvent {
    private long couponId;
    private long userId;
    private String name;
    private int discount;
}

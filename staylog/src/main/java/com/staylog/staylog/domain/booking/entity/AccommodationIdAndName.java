package com.staylog.staylog.domain.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Alias("accommodationIdAndName")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationIdAndName {
    private Long accommodationId;
    private String accommodationName;
}

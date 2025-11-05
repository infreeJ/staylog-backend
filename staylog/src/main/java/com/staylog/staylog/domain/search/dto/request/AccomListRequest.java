package com.staylog.staylog.domain.search.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AccomListRequest {
    private List<String> regionCodes; // 여러 지역 선택 가능
    private Integer people;           // 총 인원 수 (성인+어린이+유아 합쳐도 OK)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate checkIn;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate checkOut;
    private String order;          // lowPrice, highPrice, new, popular
    private Long lastAccomId;         // 무한스크롤 마지막 ID
}

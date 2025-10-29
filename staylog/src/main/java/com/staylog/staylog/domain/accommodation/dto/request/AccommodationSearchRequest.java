package com.staylog.staylog.domain.accommodation.dto.request;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationSearchRequest {
    
    private Timestamp checkInDate; 
    private Timestamp checkOutDate;
    
    private int adults;
}
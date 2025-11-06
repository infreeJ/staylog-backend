package com.staylog.staylog.domain.admin.reservation.dto.response;


import com.staylog.staylog.domain.admin.reservation.dto.AdminReservationDto;
import com.staylog.staylog.global.common.response.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminReservationListResponse {
    private List<AdminReservationDto> reservations;
    private PageResponse page;
}

package com.staylog.staylog.domain.home.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeAccommodationListRequest {

	private String regionCode; //optional
	private String sort; //rating | review | price | latest
	private int offset; //0부터 시작
	private int limit; //페이지 크기
}

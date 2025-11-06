package com.staylog.staylog.domain.admin.commonCode.dto;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관리자 공통 코드 응답 DTO (SelectBox/드롭다운 사용 목적)
 * * - SelectBox의 option 태그를 구성하기 위한 최소 단위 데이터입니다.
 */
@Alias("AdminCommonCodeDto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCommonCodeDto {
	
	/**
	 * SelectBox의 실제 값 (value 속성)
	 * * - DB 조회, API 호출 시 사용되는 고유 식별자 (ID) 역할을 합니다.
	 * - 예: 'BOARD_REVIEW', 'USER_STATUS' 등
	 */
	private String codeId;
	
	/**
	 * SelectBox의 사용자 표시 값 (화면에 보이는 텍스트)
	 * * - 사용자가 SelectBox에서 선택할 때 확인하는 이름입니다.
	 * - 예: '리뷰 게시판', '정상', '서울' 등
	 */
	private String codeName;
}
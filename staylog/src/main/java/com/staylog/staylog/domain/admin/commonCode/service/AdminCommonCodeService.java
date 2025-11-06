package com.staylog.staylog.domain.admin.commonCode.service;

import java.util.List;

import com.staylog.staylog.domain.admin.commonCode.dto.AdminCommonCodeDto;

/**
 * 관리자 공통 코드 조회 서비스 인터페이스
 *
 * - 관리자 페이지의 SelectBox나 필터 등에서 사용되는 옵션 데이터를 제공합니다.
 * - 특정 카테고리(codeId)에 속하는 하위 코드 목록을 조회하는 단일 기능을 담당합니다.
 */
public interface AdminCommonCodeService {

    /**
     * 특정 상위 코드 ID(Parent ID)에 해당하는 하위 공통 코드 목록을 조회합니다.
     * * - 프론트엔드에서 SelectBox, 라디오 버튼 등의 옵션 목록을 구성하는 데 사용됩니다.
     * - 예: codeId='BOARD_TYPE'을 입력하면, '리뷰'(BOARD_REVIEW), '저널'(BOARD_JOURNAL) 항목을 반환합니다.
     * @param codeId 조회하려는 상위 공통 코드의 ID (Parent ID)
     * @return 하위 공통 코드 ID와 이름을 담은 DTO 목록 (AdminCommonCodeDto의 commonId, commonName)
     */
    List<AdminCommonCodeDto> getCodeNameList(String codeId);
}

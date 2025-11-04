package com.staylog.staylog.domain.common.service;

import com.staylog.staylog.domain.common.dto.CommonCodeDto;
import com.staylog.staylog.domain.common.dto.response.CommonCodeGroupResponse;

import java.util.List;

public interface CommonCodeService {

    /**
     * 부모 코드로 자식 코드 목록 조회
     * @param parentId 부모 코드 ID
     * @return 자식 코드 목록
     */
    List<CommonCodeDto> getCodesByParentId(String parentId);

    /**
     * 모든 공통코드를 그룹별로 조회
     * @return 그룹별 공통코드
     */
    CommonCodeGroupResponse getAllCommonCodesGrouped();
}

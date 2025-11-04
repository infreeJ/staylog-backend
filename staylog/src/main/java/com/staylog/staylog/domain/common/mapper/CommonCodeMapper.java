package com.staylog.staylog.domain.common.mapper;

import com.staylog.staylog.domain.common.dto.CommonCodeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommonCodeMapper {

    /**
     * 부모 코드 ID로 자식 코드 목록 조회
     * @param parentId 부모 코드 ID (예: REGION_TYPE)
     * @return 자식 코드 목록
     */
    List<CommonCodeDto> getCodesByParentId(@Param("parentId") String parentId);

    /**
     * 모든 공통코드 조회 (USE_YN = 'Y'인 것만)
     * @return 모든 공통코드 목록
     */
    List<CommonCodeDto> getAllCommonCodes();
}

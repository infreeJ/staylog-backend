package com.staylog.staylog.domain.admin.commonCode.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.staylog.staylog.domain.admin.commonCode.dto.AdminCommonCodeDto;

@Mapper
public interface AdminCommonCodeMapper {

	/**
     *  코드명 조회
     * @param AdminCommonCodeDto 검색 조건 (코드 id)
     * @return 숙소 목록
     */
    List<AdminCommonCodeDto> selectCodeNameList(@Param("codeId") String codeId);
  
}

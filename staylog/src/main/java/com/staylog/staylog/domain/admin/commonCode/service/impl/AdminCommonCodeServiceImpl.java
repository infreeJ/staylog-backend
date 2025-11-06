package com.staylog.staylog.domain.admin.commonCode.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staylog.staylog.domain.admin.commonCode.dto.AdminCommonCodeDto;
import com.staylog.staylog.domain.admin.commonCode.mapper.AdminCommonCodeMapper;
import com.staylog.staylog.domain.admin.commonCode.service.AdminCommonCodeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 공통 코드 조회 서비스 구현체
 * 특정 상위 코드 ID(Parent ID)에 해당하는 하위 공통 코드 목록 조회 기능을 구현합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCommonCodeServiceImpl implements AdminCommonCodeService{
	
	private final AdminCommonCodeMapper mapper;
	
	/**
	 * 특정 상위 코드 ID에 해당하는 하위 공통 코드 목록을 DB에서 조회합니다.
	 * @param codeId 조회 대상이 되는 상위 코드 ID (Parent ID)
	 * @return 하위 공통 코드 목록 (List<AdminCommonCodeDto>)
	 */
	@Override
	public List<AdminCommonCodeDto> getCodeNameList(String codeId) {
		
		// 입력 파라미터 유효성 검사 (codeId가 null/비어있는지)
        if (codeId == null || codeId.isEmpty()) {
            log.warn("공통 코드 조회 요청 실패: codeId가 null이거나 비어있습니다.");
        }

		// Mapper 를 통해 DB에서 코드 목록을 조회
        List<AdminCommonCodeDto> codeList = mapper.selectCodeNameList(codeId);
        
        // 3. (선택 사항) 조회된 목록이 비어있을 경우 로그 기록
        if (codeList.isEmpty()) {
             log.info("공통 코드 목록 조회 완료 (codeId: {}): 조회된 코드가 없습니다.", codeId);
        } else {
             log.debug("공통 코드 목록 조회 완료 (codeId: {}): 총 {}개 항목", codeId, codeList.size());
        }

		return codeList;
	}
}

package com.staylog.staylog.domain.admin.commonCode.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.admin.commonCode.dto.AdminCommonCodeDto;
import com.staylog.staylog.domain.admin.commonCode.service.AdminCommonCodeService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 공통 코드 조회 API Controller
 *
 * - SelectBox, 드롭다운 등에서 사용될 공통 코드 목록을 제공합니다.
 */
@Slf4j
@Tag(name = "AdminCommonCodeController", description = "공통코드 코드명 조회 API")
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class AdminCommonCodeController {

    private final AdminCommonCodeService commonCodeService;
    private final MessageUtil messageUtil;

    /**
     * 특정 상위 코드 ID에 해당하는 하위 공통 코드 목록을 조회합니다.
     * @param codeId 조회하려는 상위 공통 코드의 ID (Parent ID)
     * @return 성공 시, 하위 코드 목록을 담은 성공 응답 (SuccessResponse)
     */
    @Operation(
        summary = "공통코드 코드명 조회",
        description = "공통코드의 코드 id 로 코드명을 조회합니다."
    )
    @GetMapping("/commonCode")
    public ResponseEntity<SuccessResponse<List<AdminCommonCodeDto>>> getList(
            @Parameter(description = "코드 ID") String codeId) {
    	List<AdminCommonCodeDto> list= commonCodeService.getCodeNameList(codeId);
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.name();
        SuccessResponse<List<AdminCommonCodeDto>> success = SuccessResponse.of(code, message, list);
        return ResponseEntity.ok(success);
    }
}

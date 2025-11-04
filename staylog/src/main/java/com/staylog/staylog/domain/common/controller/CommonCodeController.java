package com.staylog.staylog.domain.common.controller;

import com.staylog.staylog.domain.common.dto.CommonCodeDto;
import com.staylog.staylog.domain.common.dto.response.CommonCodeGroupResponse;
import com.staylog.staylog.domain.common.service.CommonCodeService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CommonCode", description = "공통코드 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/common-codes")
public class CommonCodeController {

    private final CommonCodeService commonCodeService;
    private final MessageUtil messageUtil;

    /**
     * 부모 코드로 자식 코드 목록 조회
     * GET /v1/common-codes?parentId=REGION_TYPE
     */
    @Operation(summary = "자식 코드 조회", description = "부모 코드 ID로 자식 코드 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<SuccessResponse<List<CommonCodeDto>>> getCodesByParentId(
            @Parameter(description = "부모 코드 ID (예: REGION_TYPE)", example = "REGION_TYPE")
            @RequestParam String parentId
    ) {
        log.info("공통코드 조회 요청 - parentId: {}", parentId);

        List<CommonCodeDto> codes = commonCodeService.getCodesByParentId(parentId);

        String code = SuccessCode.SUCCESS.name();
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        SuccessResponse<List<CommonCodeDto>> response = SuccessResponse.of(code, message, codes);

        log.info("공통코드 조회 성공 - parentId: {}, count: {}", parentId, codes.size());
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 공통코드를 그룹별로 조회
     * @author 임채호
     */

    @Operation(summary = "전체 공통코드 조회", description = "모든 공통코드를 그룹별로 한 번에 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<SuccessResponse<CommonCodeGroupResponse>> getAllCommonCodes() {
        log.info("전체 공통코드 조회 요청");

        CommonCodeGroupResponse groupedCodes = commonCodeService.getAllCommonCodesGrouped();

        String code = SuccessCode.SUCCESS.name();
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        SuccessResponse<CommonCodeGroupResponse> response = SuccessResponse.of(code, message, groupedCodes);

        log.info("전체 공통코드 조회 성공");
        return ResponseEntity.ok(response);
    }
}

package com.staylog.staylog.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 공통코드 단일 항목 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonCodeDto {
    private String codeId;           // CODE_ID (예: REGION_SEOUL)
    private String parentId;         // PARENT_ID (예: REGION_TYPE)
    private String codeName;         // CODE_NAME (한글명: 서울)
    private String codeNameEn;       // CODE_NAME_EN (영문명: Seoul)
    private String description;      // DESCRIPTION
    private Integer depth;           // DEPTH
    private Integer displayOrder;    // DISPLAY_ORDER
    private String useYn;            // USE_YN
    private String attr1;            // ATTR1 (추가 속성)
    private String attr2;            // ATTR2
    private String attr3;            // ATTR3
    private String attr4;            // ATTR4
    private String attr5;            // ATTR5
}

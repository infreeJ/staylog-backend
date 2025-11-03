package com.staylog.staylog.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDto {
    private String fileName; // UUID_OriginalName (실제 파일명)
    private String originalName; // 원본 파일명
    private String relativePath; // YYYY/MM/DD/UUID_OriginalName (저장된 상대 경로)
}
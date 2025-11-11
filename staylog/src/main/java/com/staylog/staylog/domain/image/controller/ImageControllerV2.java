package com.staylog.staylog.domain.image.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staylog.staylog.domain.image.service.ImageService;
import com.staylog.staylog.global.common.util.MessageUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "이미지 관리 컨트롤러 (v2)", description = "이미지 업로드, 수정, 삭제 기능을 담당합니다.")
@Slf4j
@RestController
@RequestMapping("/v2")
@RequiredArgsConstructor
public class ImageControllerV2 {

	
}

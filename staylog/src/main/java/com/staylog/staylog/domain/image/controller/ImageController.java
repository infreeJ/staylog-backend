package com.staylog.staylog.domain.image.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 이미지 다운로드/로드 컨트롤러
 * @author 고윤제
 * 
 */
@Tag(name = "ImageController", description = "Image Load & Upload")
@Slf4j
@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class ImageController {
	
}

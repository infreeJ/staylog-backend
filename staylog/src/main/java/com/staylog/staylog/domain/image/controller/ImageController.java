package com.staylog.staylog.domain.image.controller;

import com.staylog.staylog.domain.image.dto.ImageServeDto;
import com.staylog.staylog.domain.image.service.ImageService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "ImageController", description = "Image Load & Upload")
@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final MessageUtil messageUtil;

    @Operation(summary = "이미지 업로드", description = "이미지를 업로드하는 대상 ID와 연결하여 DB에 저장합니다.")
    @PostMapping("/images/upload")
    public ResponseEntity<SuccessResponse<List<ImageServeDto>>> uploadImages(@RequestParam("files") List<MultipartFile> files,
                                           @RequestParam("targetType") String targetType,
                                           @RequestParam("targetId") long targetId) {
        List<ImageServeDto> imagesResponse = imageService.saveImages(files, targetType, targetId);
        String message = messageUtil.getMessage(SuccessCode.IMAGE_UPLOAD_SUCCESS.getMessageKey());
        String code = SuccessCode.IMAGE_UPLOAD_SUCCESS.getCode();
        SuccessResponse<List<ImageServeDto>> success = SuccessResponse.of(code, message, imagesResponse);
        return ResponseEntity.ok(success);
    }

    @Operation(summary = "대상 타입과 고유번호로 이미지 불러오기", description = "대상 정보(type,id)에 해당하는 모든 이미지를 List로 불러옵니다.")
    @GetMapping("/images/{targetType}/{targetId}")
    public ResponseEntity<SuccessResponse<List<ImageServeDto>>> getImagesByTarget(@PathVariable String targetType,
                                                                             @PathVariable long targetId) {
        List<ImageServeDto> imagesResponse = imageService.getImagesByTarget(targetType, targetId);

        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey()); // 일반적인 성공 메시지 가정
        String code = SuccessCode.SUCCESS.getCode();
        SuccessResponse<List<ImageServeDto>> success = SuccessResponse.of(code, message, imagesResponse);
        return ResponseEntity.ok(success);
    }
    
    @Operation(summary = "단일 이미지 업로드", description = "단일 이미지를 저장합니다")
    @PostMapping("/profile/upload")
    public ResponseEntity<SuccessResponse<String>> saveProfileImage(MultipartFile file, @RequestParam("userId") Long userId){
    	
    	return null;
    }
    
    
}
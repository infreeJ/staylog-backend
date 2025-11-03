package com.staylog.staylog.domain.image.controller;

import com.staylog.staylog.domain.image.dto.ImageDto;
import com.staylog.staylog.domain.image.service.ImageService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "ImageController", description = "Image Load & Upload")
@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final MessageUtil messageUtil;

    @Operation(summary = "Upload Multiple Images", description = "Uploads multiple images and links them to a target entity.")
    @PostMapping("/images/upload")
    public ResponseEntity<SuccessResponse<List<ImageDto>>> uploadImages(@RequestParam("files") List<MultipartFile> files,
                                           @RequestParam("targetType") String targetType,
                                           @RequestParam("targetId") Long targetId) {
        try {
            List<ImageDto> savedImages = imageService.saveImages(files, targetType, targetId);

            String message = messageUtil.getMessage(SuccessCode.IMAGE_UPLOAD_SUCCESS.getMessageKey());
            String code = SuccessCode.IMAGE_UPLOAD_SUCCESS.getCode();
            SuccessResponse<List<ImageDto>> response = SuccessResponse.of(code, message, savedImages);

            return new ResponseEntity<>(response, HttpStatus.valueOf(SuccessCode.IMAGE_UPLOAD_SUCCESS.getHttpStatus()));
        } catch (IOException e) {
            log.error("Image upload failed", e);
            // In a real app, you'd return a structured ErrorResponse here
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument for image upload", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
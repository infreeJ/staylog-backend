package com.staylog.staylog.domain.image.service;

import com.staylog.staylog.domain.image.dto.ImageServeDto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 이미지 저장 서비스 인터페이스
 * @author 고윤제
 */
public interface ImageService {
    List<ImageServeDto> saveImages(List<MultipartFile> files, String targetType, long targetId);
    List<ImageServeDto> getImagesByTarget(String targetType, long targetId);
    List<ImageServeDto> updateImages(List<MultipartFile> files);
}

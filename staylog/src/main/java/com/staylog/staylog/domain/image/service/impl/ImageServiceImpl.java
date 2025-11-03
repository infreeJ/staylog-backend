package com.staylog.staylog.domain.image.service.impl;

import com.staylog.staylog.domain.image.dto.ImageDto;
import com.staylog.staylog.domain.image.mapper.ImageMapper;
import com.staylog.staylog.domain.image.service.ImageService;
import com.staylog.staylog.global.common.dto.FileUploadDto;
import com.staylog.staylog.global.common.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageMapper imageMapper;

    @Value("${file.image-location}")
    private String uploadPath;

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, String targetType, Long targetId) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Files are empty");
        }

        List<ImageDto> savedImages = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // 빈 파일은 스킵
            }

            FileUploadDto fileUploadDto = FileUtil.saveFile(file, uploadPath);

            // displayOrder를 읽어와서 다음 순서로 넣어준다
            Long displayOrder = imageMapper.selectMaxDisplayOrder(targetType, targetId);

            ImageDto imageDto = ImageDto.builder()
                    .imageType("IMG_ORIGINAL")
                    .targetType(targetType)
                    .targetId(String.valueOf(targetId))
                    .savedUrl(fileUploadDto.getRelativePath()) // savedUrl 필드에 FileUtil에서 반환된 상대 경로 저장
                    .originalName(fileUploadDto.getOriginalName())
                    .fileSize(String.valueOf(file.getSize()))
                    .mimeType(file.getContentType())
                    .displayOrder(displayOrder) // 받아온 displayOrder값을 넣어준다
                    .build();

            imageMapper.insertImage(imageDto);
        }

        // After all inserts, select all images for the target to get fully populated DTOs
        return imageMapper.selectImagesByTarget(targetType, targetId);
    }

    @Override
    public List<ImageDto> getImagesByTarget(String targetType, Long targetId) {
        List<ImageDto> images = imageMapper.selectImagesByTarget(targetType, targetId);
        // 각 이미지 DTO에 imageUrl 필드 추가
        for (ImageDto image : images) {
            // URL은 /images/{savedName} 형태로 구성
            image.setImageUrl("/images/" + image.getSavedUrl());
        }
        return images;
    }
}
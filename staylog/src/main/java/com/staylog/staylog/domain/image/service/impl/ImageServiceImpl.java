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
import java.time.LocalDate;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageMapper imageMapper;

    @Value("${file.image-location}")
    private String uploadPath;

    @Override
    public java.util.List<ImageDto> saveImages(java.util.List<MultipartFile> files, String targetType, Long targetId) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Files are empty");
        }

        java.util.List<ImageDto> savedImages = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // Skip empty files
            }

            FileUploadDto fileUploadDto = FileUtil.saveFile(file, uploadPath);

            ImageDto imageDto = ImageDto.builder()
                    .imageType("IMG_ORIGINAL")
                    .targetType(targetType)
                    .targetId(String.valueOf(targetId))
                    .savedName(fileUploadDto.getSavedName())
                    .originalName(fileUploadDto.getOriginalName())
                    .fileSize(String.valueOf(file.getSize()))
                    .mimeType(file.getContentType())
                    .build();

            imageMapper.insertImage(imageDto);
            savedImages.add(imageDto);
        }

        return savedImages;
    }
}
package com.staylog.staylog.domain.image.service;

import com.staylog.staylog.domain.image.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    List<ImageDto> saveImages(List<MultipartFile> files, String targetType, Long targetId) throws IOException;

    List<ImageDto> getImagesByTarget(String targetType, Long targetId); // 이미지 목록 조회 메서드 추가

}

package com.staylog.staylog.domain.image.service;

import com.staylog.staylog.domain.image.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    List<ImageDto> saveImages(List<MultipartFile> files, String targetType, Long targetId) throws IOException;

}

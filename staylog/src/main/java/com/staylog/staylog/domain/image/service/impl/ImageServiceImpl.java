package com.staylog.staylog.domain.image.service.impl;

import com.staylog.staylog.domain.image.dto.ImageDto;
import com.staylog.staylog.domain.image.dto.ImageServeDto;
import com.staylog.staylog.domain.image.mapper.ImageMapper;
import com.staylog.staylog.domain.image.service.ImageService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.common.dto.FileUploadDto;
import com.staylog.staylog.global.common.util.FileUtil;
import com.staylog.staylog.global.common.util.MessageUtil;
import com.staylog.staylog.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageMapper imageMapper;
    private final MessageUtil messageUtil;

    @Value("${file.image-location}")
    private String uploadPath;

    @Override
    public List<ImageServeDto> saveImages(List<MultipartFile> files, String targetType, long targetId){
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Files are empty");
        }
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // 빈 파일은 스킵
            }

            FileUploadDto fileUploadDto = null;
    		try {
    			fileUploadDto = FileUtil.saveFile(file, uploadPath);
	        } catch (IOException e) {
	            log.error("이미지 업로드 중 IO 오류 발생", e);
	            // GlobalExceptionHandler에서 처리하도록 예외를 다시 던집니다.
	            String message = messageUtil.getMessage(ErrorCode.FILE_UPLOAD_FAILED.getMessageKey());
	            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, message, e);
	        } catch (IllegalArgumentException e) {
	            log.error("이미지 업로드 인자 오류 발생", e);
	            // GlobalExceptionHandler에서 처리하도록 예외를 다시 던집니다.
	            String message = messageUtil.getMessage(ErrorCode.INVALID_FILE_TYPE.getMessageKey());
	            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE, message, e);
	        }

            // displayOrder를 읽어와서 다음 순서로 넣어준다
            Long displayOrder = imageMapper.selectMaxDisplayOrder(targetType, targetId);

            ImageDto imageDto = ImageDto.builder()
                    .imageType("IMG_ORIGINAL")
                    .targetType(targetType)
                    .targetId(targetId)
                    .savedUrl(fileUploadDto.getRelativePath()) // savedUrl 필드에 FileUtil에서 반환된 상대 경로 저장
                    .originalName(fileUploadDto.getOriginalName())
                    .fileSize(String.valueOf(file.getSize()))
                    .mimeType(file.getContentType())
                    .displayOrder(displayOrder) // 받아온 displayOrder값을 넣어준다
                    .build();

            imageMapper.insertImage(imageDto);
        }
        
        // 이미지가 DB에 저장된 후 해당 타겟에 맞는 이미지를 다시 Select 해서 리턴해준다.
        return this.getImagesByTarget(targetType, targetId);
    }

    @Override
    public List<ImageServeDto> getImagesByTarget(String targetType, long targetId) {
        List<ImageDto> images = imageMapper.selectImagesByTarget(targetType, targetId);
        // 각 이미지 DTO에 imageUrl 필드 추가
        List<ImageServeDto> imagesServe = new ArrayList<>();
        for (ImageDto image : images) {
            imagesServe.add(
	    		ImageServeDto.builder()
	    					 .imageId(image.getImageId())
			       			 .imageUrl("/images/" + image.getSavedUrl())
			       			 .targetType(image.getTargetType())
			       			 .targetId(image.getTargetId())
			       			 .displayOrder(image.getDisplayOrder())
			       			 .build());
        }
        return imagesServe;
    }

	@Override
	public List<ImageServeDto> updateImages(List<MultipartFile> files) {
		
		return null;
	}

}
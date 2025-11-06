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

    // displayOrder 계산 로직 보호용 락 객체
    private final Object displaObjectLock = new Object();
    
    @Override
    public List<ImageServeDto> saveImages(List<MultipartFile> files, String targetType, long targetId){
    	log.info("ImageService.saveImages 호출됨 - targetType: {}, targetId: {}", targetType, targetId);
    	String prefixedTargetType = "IMG_FROM_"+targetType;
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
        
        long displayOrder;
        synchronized (displaObjectLock) {
        	// displayOrder를 읽어와서 다음 순서로 넣어준다
        	displayOrder = imageMapper.selectMaxDisplayOrder(prefixedTargetType, targetId);	
		}
        
        List<ImageDto> newImages = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // 빈 파일은 스킵
            }

            FileUploadDto fileUploadDto = null;
    		try {
    			fileUploadDto = FileUtil.saveFile(file, uploadPath);
    			log.info("저장된 파일 경로"+uploadPath);
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
    		
            ImageDto imageDto = ImageDto.builder()
                    .imageType("IMG_ORIGINAL")
                    .targetType(prefixedTargetType)
                    .targetId(targetId)
                    .savedUrl(fileUploadDto.getRelativePath()) // savedUrl 필드에 FileUtil에서 반환된 상대 경로 저장
                    .originalName(fileUploadDto.getOriginalName())
                    .fileSize(String.valueOf(file.getSize()))
                    .mimeType(file.getContentType())
                    .displayOrder(displayOrder++) // 받아온 displayOrder값을 넣어준다.
                    .build();

            imageMapper.insertImage(imageDto);
            // 방금 삽입된 ImageDto를 반환 리스트에 추가
            newImages.add(imageDto);
        }
        
        // 새로 업로드된 ImageDto 리스트를 ImageServeDto 리스트로 변환하여 반환.
        return convertToImageServeDtoList(newImages);
    }

    @Override
    public List<ImageServeDto> getImagesByTarget(String targetType, long targetId) {
    	String prefixedTargetType = "IMG_FROM_"+targetType;
        List<ImageDto> images = imageMapper.selectImagesByTarget(prefixedTargetType, targetId);
        
        return convertToImageServeDtoList(images);
    }

	@Override
	public List<ImageServeDto> updateImages(List<MultipartFile> files) {
		
		return null;
	}

	
	//정나영
	@Override
	public Long getBoardId() {
		
		//boardId 만 미리 발급 = 이미지 테이블의 TargetId 
		Long imageId = imageMapper.getNextBoardId();
		
		return imageId;
	}

	/**
	 * ImageDto 리스트를 프론트 전달용 ImageServeDto 리스트로 변환
	 * 이 과정에서 targetType의 'IMG_FROM_' 접두사 제거.
	 * 
	 */
	private List<ImageServeDto> convertToImageServeDtoList(List<ImageDto> imageDtos) {
		List<ImageServeDto> imagesServe = new ArrayList<>();
		for (ImageDto image : imageDtos) {
            String cleanTargetType = image.getTargetType();
           if (cleanTargetType != null && cleanTargetType.startsWith("IMG_FROM_")) {
               cleanTargetType = cleanTargetType.substring("IMG_FROM_".length());
           }
           ImageServeDto imageServeDto = ImageServeDto.builder()
					    .imageId(image.getImageId())
					    .imageUrl("/images/" + image.getSavedUrl())
					    .targetType(cleanTargetType)
					    .targetId(image.getTargetId())
					    .displayOrder(image.getDisplayOrder())
					    .build();
           imagesServe.add(imageServeDto);
        }
		
		return imagesServe;
	}
}
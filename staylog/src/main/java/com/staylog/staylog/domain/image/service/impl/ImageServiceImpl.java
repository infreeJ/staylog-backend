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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageMapper imageMapper;
    private final MessageUtil messageUtil;

    @Value("${file.image-location}")
    private String uploadPath;

    // displayOrder 계산 로직 보호용 락 객체 -> DB 잠금으로 대체되어 더 이상 필요 없음
//	private final Object displaObjectLock = new Object();
    
    @Override
    public List<ImageServeDto> saveImages(List<MultipartFile> files, String targetType, long targetId){
    	log.info("ImageService.saveImages 호출됨 - targetType: {}, targetId: {}", targetType, targetId);
    	String prefixedTargetType = "IMG_FROM_"+targetType;
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
        

        // 필요한 displayOrder 개수만큼 한 번에 예약하고 시작 번호를 가져온다.
        long startOrder = DisplayOrderRange(prefixedTargetType, targetId, files.size());
        long currentDisplayOrder = startOrder;
        
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
                    .displayOrder(currentDisplayOrder++)
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
	
	/**
	 * 필요한 개수(count)만큼의 displayOrder 범위를 예약하고, 시작 번호를 반환.
	 * 
	 * @param targetType 접두사가 붙은 targetType
	 * @param targetId
	 * @param count 예약할 displayOrder의 개수
	 * @return 예약된 displayOrder의 시작 번호
	 */
	@SuppressWarnings("unused")
	private long DisplayOrderRange(String targetType, long targetId, int count) {
		Map<String, Object> params = new HashMap<>();
		params.put("targetType", targetType);
		params.put("targetId", targetId);
		
		while(true) {
			// 1. 행을 잠그고 현재 카운터 값을 조회
		    Long startOrder = imageMapper.selectForUpdate(params);
		
		    if (startOrder != null) {
		        // 2a. 카운터가 존재하면, 필요한 만큼(count) 증가시킨 값을 DB에 저장
		        long nextOrder = startOrder + count;
		        params.put("nextDisplayOrder", nextOrder);
		        imageMapper.updateCounter(params);
		        return startOrder; // 시작 번호 반환
		    } else {
		        // 2b. 카운터가 없으면 새로 생성
		        try {
		            long nextOrder = 1L + count;
		            params.put("nextDisplayOrder", nextOrder);
		            imageMapper.createCounter(params);
		            return 1L; // 시작 번호는 1
		        } catch (DuplicateKeyException e) {
		            // 3. INSERT가 실패하면 (다른 트랜잭션이 방금 생성), 다시 시도
		            log.warn("Race condition detected on counter creation for {}-{}, retrying.", targetType, targetId);
		            // 다음 루프에서는 이미 생성된 카운터를 조회(selectForUpdate)하여 처리함.
		            continue;
		        }
		    }
		}
	}
}
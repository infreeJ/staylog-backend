package com.staylog.staylog.domain.image.mapper;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param; // Import Param annotation

import com.staylog.staylog.domain.image.dto.ImageDto;

import java.util.List;

@Mapper
public interface ImageMapper {
	void insertImage(ImageDto imageDto);
	ImageDto selectProfileByUserId(String targetType, @Param("targetId") long userId);

	Long selectForUpdate(ImageDto imageDto);
	void updateCounter(ImageDto imageDto);
	void createCounter(ImageDto imageDto);

	void deleteImage(long imageId);
	void updateDisplayOrderAfterDelete(ImageDto imageDto);
	void decrementCounter(ImageDto imageDto);
	
	List<ImageDto> selectImagesByTarget(String targetType, long targetId);
	ImageDto selectImageById(long imageId);
	// 일괄 삭제
	void deleteImagesByTarget(ImageDto imageDto);
	void deleteCounterByTarget(ImageDto imageDto);
	/**
	 * 특정 이미지의 displayOrder를 업데이트.
	 * @param imageDto imageId와 변경될 displayOrder 값을 담은 Dto
	 */
	void updateImageDisplayOrder(ImageDto imageDto);
	void setCounterValue(ImageDto imageDto);
	
	/**
     * 여러 targetId에 해당하는 이미지 목록을 조회 (N+1 문제 해결용)
     * @param targetType 이미지 타입
     * @param targetIds 조회할 target ID 목록
     * @return 이미지 DTO 목록
     */
    List<ImageDto> selectImagesByTargetIds(@Param("targetType") String targetType,
    									@Param("targetIds") List<Long> targetIds);
    
    /**
     * 여러 targetId에 대한 대표이미지 조회 (displayOrder기준)
     * @param targetType 이미지 타입
     * @param targetIds 조회할 target ID 목록
     * @return 대표 이미지 DTO 목록
     */
    List<ImageDto> selectFirstImageByTargetIds(String targetType, List<Long> targetIds);
}
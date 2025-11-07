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
	
	//정나영 : 보드 아이디만(타겟 아이디)만
	Long getNextBoardId();
}
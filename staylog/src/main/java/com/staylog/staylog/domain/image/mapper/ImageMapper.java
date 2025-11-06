package com.staylog.staylog.domain.image.mapper;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param; // Import Param annotation

import com.staylog.staylog.domain.image.dto.ImageDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ImageMapper {
	void insertImage(ImageDto imageDto);
	ImageDto selectProfileByUserId(String targetType, @Param("targetId") long userId);

	Long selectForUpdate(Map<String, Object> params);
	void updateCounter(Map<String, Object> params);
	void createCounter(Map<String, Object> params);

	void deleteImage(long imageId);
	void updateDisplayOrderAfterDelete(Map<String, Object> params);
	void decrementCounter(Map<String, Object> params);
	
	List<ImageDto> selectImagesByTarget(String targetType, long targetId);

	
	//정나영 : 보드 아이디만(타겟 아이디)만
	Long getNextBoardId();
}
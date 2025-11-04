package com.staylog.staylog.domain.image.mapper;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param; // Import Param annotation

import com.staylog.staylog.domain.image.dto.ImageDto;

import java.util.List;

@Mapper
public interface ImageMapper {
	void insertImage(ImageDto imageDto);
	Long selectMaxDisplayOrder(String targetType, Long targetId);
	List<ImageDto> selectImagesByTarget(String targetType, Long targetId);
	ImageDto selectProfileByUserId(String targetType, @Param("targetId") Long userId);
}
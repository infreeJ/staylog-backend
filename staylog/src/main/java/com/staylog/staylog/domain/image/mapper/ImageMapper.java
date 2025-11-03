package com.staylog.staylog.domain.image.mapper;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param; // Import Param annotation

import com.staylog.staylog.domain.image.dto.ImageDto;

import java.util.List;

@Mapper
public interface ImageMapper {
	void insertImage(ImageDto imageDto);
	Long selectMaxDisplayOrder(@Param("targetType") String targetType, @Param("targetId") Long targetId);
	List<ImageDto> selectImagesByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId); // Added method
}
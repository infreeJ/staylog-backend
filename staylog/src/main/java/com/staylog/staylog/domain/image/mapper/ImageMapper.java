package com.staylog.staylog.domain.image.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.staylog.staylog.domain.image.dto.ImageDto;

@Mapper
public interface ImageMapper {
	void insertImage(ImageDto imageDto);
}

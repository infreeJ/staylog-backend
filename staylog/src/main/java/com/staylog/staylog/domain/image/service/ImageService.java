package com.staylog.staylog.domain.image.service;

import com.staylog.staylog.domain.image.dto.ImageDto;

public interface ImageService {
	
	public String[] saveImage(ImageDto imageDto);
	public void updateImage();
	
}

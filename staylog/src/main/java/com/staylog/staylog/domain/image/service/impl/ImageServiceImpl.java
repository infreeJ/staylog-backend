package com.staylog.staylog.domain.image.service.impl;

import java.awt.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staylog.staylog.domain.image.dto.ImageDto;
import com.staylog.staylog.domain.image.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
	
	@Value("${file.image-location}")
	private String filePath;

	@Override
	public String[] saveImage(ImageDto imageDto) {
		
		return null;
	}

	@Override
	public void updateImage() {
		
		
	}


}

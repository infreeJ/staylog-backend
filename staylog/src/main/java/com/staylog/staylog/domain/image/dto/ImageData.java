package com.staylog.staylog.domain.image.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageData {
	private long imageId;
	private String imageUrl;
	private long displayOrder;
}

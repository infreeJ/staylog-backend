package com.staylog.staylog.domain.image.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImageDto {
	private long imageId;
	private String imageType;
	private String targetType;
	private String targetId;
	private String savedName;
	private String originalName;
	private String fileSize;
	private String mimeType;
	private long display_order;
	private String uploadDate;
}

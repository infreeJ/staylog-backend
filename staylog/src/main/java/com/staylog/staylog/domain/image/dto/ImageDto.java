package com.staylog.staylog.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
	private long imageId;
	private String imageType;
	private String targetType;
	private String targetId;
	private String savedName;
	private String originalName;
	private String fileSize;
	private String mimeType;
	private long displayOrder;
	private String uploadDate;
}

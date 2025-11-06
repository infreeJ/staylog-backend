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
public class ImageServeDto {
	private long imageId;
	private String targetType;
	private long targetId;
	private long displayOrder;
	private String imageUrl;
}

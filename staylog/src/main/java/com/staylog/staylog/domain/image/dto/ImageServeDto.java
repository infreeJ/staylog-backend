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
	private Long targetId;
	private Long displayOrder;
	private String imageUrl;
}

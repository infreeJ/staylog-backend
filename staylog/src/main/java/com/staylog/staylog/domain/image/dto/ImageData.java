package com.staylog.staylog.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageData {
	private long imageId;
	private String imageUrl;
	private long displayOrder;
	private String originalName;
}

package com.staylog.staylog.domain.image.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageResponse {
	private String targetType;
	private long targetId;
	List<ImageData> images;
}

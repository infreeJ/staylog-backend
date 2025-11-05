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
	private long targetId;
	private String savedUrl; // DB 컬럼명 변경에 따라 savedName을 savedUrl로 변경
	private String originalName;
	private String fileSize;
	private String mimeType;
	private long displayOrder;
	private String uploadDate;
	private String imageUrl; // 이미지 URL 필드 추가
}

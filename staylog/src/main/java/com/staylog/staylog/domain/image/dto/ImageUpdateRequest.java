package com.staylog.staylog.domain.image.dto;

import java.util.List;

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
public class ImageUpdateRequest {
	private String targetType;
	private long targetId;
	private List<ImageUpdateItemDto> imageOrders;
}

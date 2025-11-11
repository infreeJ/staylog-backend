package com.staylog.staylog.domain.accommodation.dto.response;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.staylog.staylog.domain.image.dto.ImageData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Alias("ReviewResponse")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
	private Long boardId;
	private String nickname;
	private String profileImage;
	private Double rating;
	private int reviewCount;
	private String title;
	private String content;
	private ImageData images;
	private Timestamp createdAt;
}

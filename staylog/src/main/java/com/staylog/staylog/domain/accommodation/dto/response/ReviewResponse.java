package com.staylog.staylog.domain.accommodation.dto.response;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

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
	private String content;
	private Timestamp createdAt;
}

package com.staylog.staylog.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardRequest {

    @NotBlank
    private String boardType;   // 게시판 카테고리
    @NotBlank
    private String regionCode;  // 게시글 지역
    @NotBlank
    private String title;       // 게시글 제목
    @NotBlank
    private String content;     // 게시글 내용
    @NotBlank
    private Long userId;        // 작성자
    
    private Long accommodationId;    // 숙소 ID - 리뷰, 저널
    private Long bookingId;         // 예약 ID - 리뷰
    private Integer rating;         // 별점 - 리뷰
}

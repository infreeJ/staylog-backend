package com.staylog.staylog.domain.mypage.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 1:1 문의 작성 DTO
 * @Author 오미나
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryWriteRequest {

    private Long userId; //회원(작성자) 고유 ID
    private String title; //제목
    private String content; //게시글 내용
}

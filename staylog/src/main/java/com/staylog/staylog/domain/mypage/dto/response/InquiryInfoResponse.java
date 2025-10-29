package com.staylog.staylog.domain.mypage.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryInfoResponse {
    private Long id;
    private String title;
    private String content;
    private String status; // WAITING, ANSWERED
    private String question;
    private String answer;
    private String category;
    private LocalDateTime createdAt;
}


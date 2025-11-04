package com.staylog.staylog.domain.admin.user.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 *  유저 상태 변경
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateStatusResponse {
    private Long userId;
    private String userName;
    private String userStatus;

    private LocalDateTime updatedAt;
}


package com.staylog.staylog.domain.notification.dto.request;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * DB 조회용 Dto
 * @apiNote 알림 목록 조회 시 쿼리문의 결과값을 받은 용도로 사용한다.
 * details는 String Type이지만 JSON 형식으로 구성한다.
 * @author 이준혁
 */
@Alias("notificationSelectRequest")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSelectRequest {
    private Long notiId;
    private Long userId;
    private String notiType;
    private Long targetId;
    private String details;
    private String isRead;
    private LocalDateTime createdAt;
}

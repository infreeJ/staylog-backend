package com.staylog.staylog.domain.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 알림 목록 조회 및 푸시용 Dto
 * @apiNote details 필드는 타입별로 다른 객체가 들어간다
 * @author 이준혁
 */
@Alias("notificationResponse")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long notiId;
    private Long targetId;
    private DetailsResponse details;
    private String isRead;
    private LocalDateTime createdAt;
}

package com.staylog.staylog.domain.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Alias("notificationRequest")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private Long notiId; // selectKey로 가져오기
    private Long userId;
    private String notiType;
    private Long targetId;
    private String details;
}

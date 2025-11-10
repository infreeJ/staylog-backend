package com.staylog.staylog.global.event;

import com.staylog.staylog.domain.notification.dto.response.NotificationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreatedAllEvent {
    private NotificationResponse notificationResponse;
    private String batchId;
}

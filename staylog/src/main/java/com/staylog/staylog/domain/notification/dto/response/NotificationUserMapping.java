package com.staylog.staylog.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * pushBatchNotifications에서 사용할 Dto
 *      방금 저장된 단체 알림과 현재 접속한 userId를 매핑하여 가져오는 용도
 * @author 이준혁
 */
@Alias("notificationUserMapping")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUserMapping {
    private Long notiId;
    private Long userId;
}

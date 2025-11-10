package com.staylog.staylog.domain.notification.dto.request;

import lombok.*;
import org.apache.ibatis.type.Alias;

/**
 * DB 저장용 Dto
 * details는 String Type이지만 JSON 형식으로 구성한다.
 * @author 이준혁
 */
@Alias("notificationRequest")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    private Long notiId; // selectKey로 가져오기
    private Long userId;
    private String notiType;
    private Long targetId;
    private String details;
    private String batchId; // null일 수 있다.
}

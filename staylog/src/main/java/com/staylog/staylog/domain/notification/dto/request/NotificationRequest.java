package com.staylog.staylog.domain.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class NotificationRequest {
    private Long notiId; // selectKey로 가져오기
    private Long userId;
    private String notiType;
    private Long targetId;
    private String details;
}

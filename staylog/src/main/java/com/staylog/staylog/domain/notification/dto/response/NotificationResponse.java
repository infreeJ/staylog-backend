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
    private Long userId;
    private String notiType;
    private Long targetId;
    private String isRead;
    private LocalDateTime createdAt;

    @JsonTypeInfo( // 타입을 어떻게 JSON에 포함시킬지 정의
            use = JsonTypeInfo.Id.NAME, // 타입 이름을 사용 (예: NOTI_RES_CONFIRM)
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY, // 타입을 외부 프로퍼티(notiType) 기준으로 결정
            property = "notiType" // notiType 필드의 값을 보고 details의 실제 타입을 결정
    )
    @JsonSubTypes({ // notiType 값에 따라 매핑될 details의 클래스 정의
            @JsonSubTypes.Type(value = ReservationNotiDetails.class, name = "NOTI_RES_CONFIRM"),
            @JsonSubTypes.Type(value = ReservationNotiDetails.class, name = "NOTI_RES_CANCEL"),
            @JsonSubTypes.Type(value = ReviewNotiDetails.class, name = "NEW_REVIEW_CREATE"),
            @JsonSubTypes.Type(value = CommentNotiDetails.class, name = "NEW_COMMENT_CREATE"),
    })
    private Object details;
}

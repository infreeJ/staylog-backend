package com.staylog.staylog.domain.notification.mapper;

import com.staylog.staylog.domain.notification.dto.request.NotificationRequest;
import com.staylog.staylog.domain.notification.dto.request.NotificationSelectRequest;
import com.staylog.staylog.domain.notification.dto.request.ReadAllRequest;
import com.staylog.staylog.domain.notification.dto.request.ReadRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {

    /**
     * 알림 데이터 저장
     * @author 이준혁
     * @param notificationRequest 알림 정보
     * @return 성공 여부 1 또는 0
     */
    public int notiSave(NotificationRequest notificationRequest);

    /**
     * 전체 사용자 알림 일괄 데이터 저장
     * @author 이준혁
     * @param notificationRequest 알림 정보
     * @return 성공 여부 1 또는 0
     */
    public int notiSaveBroadcast(NotificationRequest notificationRequest);

    /**
     * 유저 한명의 알림 리스트 조회
     * @author 이준혁
     * @param userId 유저 PK
     * @return NotificationResponse
     */
    public List<NotificationSelectRequest> findNotificationsByUserId(long userId);



    /**
     * 알림 삭제
     * @author 이준혁
     * @param notiId 알림 PK
     * @return 성공 여부 1 또는 0
     */
    public int deleteByNotiId(long notiId);


    /**
     * 단일 알림 읽음 처리
     * @author 이준혁
     * @param readRequest 알림 PK
     * @return 성공 여부 1 또는 0
     */
    public int readOne(ReadRequest readRequest);

    /**
     * 모든 알림 읽음 처리
     * @author 이준혁
     * @param readAllRequest 유저 PK
     * @return 성공 여부 1 또는 0
     */
    public int readAll(ReadAllRequest readAllRequest);

    /**
     * 안읽은 알림 수 조회
     * @author 이준혁
     * @param userId 사용자 PK
     * @return 안읽은 알림 수
     */
    public int unreadCount(long userId);
}

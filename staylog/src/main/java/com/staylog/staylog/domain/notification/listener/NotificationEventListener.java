package com.staylog.staylog.domain.notification.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staylog.staylog.domain.board.dto.CommentsDto;
import com.staylog.staylog.domain.board.mapper.BoardMapper;
import com.staylog.staylog.domain.board.mapper.CommentsMapper;
import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.notification.dto.request.NotificationRequest;
import com.staylog.staylog.domain.notification.dto.response.DetailsResponse;
import com.staylog.staylog.domain.notification.service.NotificationService;
import com.staylog.staylog.domain.payment.mapper.PaymentMapper;
import com.staylog.staylog.domain.user.mapper.UserMapper;
import com.staylog.staylog.global.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import com.staylog.staylog.global.event.CouponCreatedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationEventListener {

    private final UserMapper userMapper;
    private final CommentsMapper commentsMapper;
    private final BoardMapper boardMapper;
    private final PaymentMapper paymentMapper;
    private final BookingMapper bookingMapper;
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;


    /**
     * 쿠폰 발급 이벤트리스너 메서드
     *
     * @param event 쿠폰 발급 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    private void handleCouponCreatedEvent(CouponCreatedEvent event) {
        long recipientId = event.getUserId(); // 수신자 PK

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("https://picsum.photos/id/10/200/300") // TODO: 이미지 삽입 필요
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title("쿠폰이 발급되었습니다!")
                .message("쿠폰함을 확인해주세요")
                .typeName("Coupon")
                .build();

        try {
            // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성
            String detailsObject = objectMapper.writeValueAsString(detailsResponse);

            // INSERT의 parameterType 객체 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(recipientId)
                    .notiType("NOTI_COUPON_CREATE")
                    .targetId(recipientId) // 이동할 페이지 PK
                    .details(detailsObject)
                    .build();

            // DB 저장 후 SSE 요청하는 메서드 호출
            notificationService.saveNotification(notificationRequest, detailsResponse);

        } catch (JsonProcessingException e) {
            log.error("handleCouponCreatedEvent 처리 중 오류 발생. event: {}", event, e);
//            throw new RuntimeException(e);
        }
    }

    /**
     * 전체 사용자 일괄 쿠폰 발급 이벤트리스너 메서드
     *
     * @param event 쿠폰 발급 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener
    private void handleCouponCreatedAllEvent(CouponCreatedAllEvent event) {

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("https://picsum.photos/id/10/200/300") // TODO: 이미지 삽입 필요
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title("쿠폰이 발급되었습니다!")
                .message("쿠폰함을 확인해주세요")
                .typeName("Coupon")
                .build();

        try {
            // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성
            String detailsObject = objectMapper.writeValueAsString(detailsResponse);

            // INSERT의 parameterType 객체 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(null)
                    .notiType("NOTI_COUPON_CREATE")
                    .targetId(null) // 이동할 페이지 PK
                    .details(detailsObject)
                    .build();

            // DB 저장 후 SSE 요청하는 메서드 호출
            notificationService.saveAllNotification(notificationRequest, detailsResponse);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 결제 승인 이벤트리스너 메서드
     *
     * @param event 결제 승인 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener
    private void handlePaymentResultEvent(PaymentConfirmEvent event) {
        Map<String, Object> payment = paymentMapper.findPaymentById(event.getPaymentId());
        Map<String, Object> recipientIdAndAccommodationName = bookingMapper.findUserIdAndAccommodationNameByBookingId(event.getBookingId());

        long recipientId = (long) recipientIdAndAccommodationName.get("userId"); // 수신자(예약자) PK
        String accommodationName = (String) recipientIdAndAccommodationName.get("accommodationName"); // 숙소명
        LocalDateTime approvedAt = (LocalDateTime) payment.get("approvedAt"); // 결제 승인 시간

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("https://picsum.photos/id/10/200/300") // TODO: 이미지 삽입 필요
                .date(approvedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(accommodationName)
                .message("예약이 확정되었습니다.")
                .typeName("Reservation")
                .build();

        try {
            // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성
            String detailsObject = objectMapper.writeValueAsString(detailsResponse);

            // INSERT의 parameterType 객체 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(recipientId)
                    .notiType("NOTI_RES_CONFIRM")
                    .targetId(event.getPaymentId()) // 이동할 페이지 PK
                    .details(detailsObject)
                    .build();

            // DB 저장 후 SSE 요청하는 메서드 호출
            notificationService.saveNotification(notificationRequest, detailsResponse);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 결제 취소 이벤트리스너 메서드
     *
     * @param event 결제 취소 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener
    private void handleRefundResultEvent(PaymentConfirmEvent event) {
        // TODO: 메서드 정의 필요
    }


    /**
     * 리뷰 게시글 작성 이벤트리스너 메서드
     *
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener
    private void handleReviewCreatedEvent(ReviewCreatedEvent event) {
        long recipientId = (long) 7;  // TODO: 원래 Admin에게 보내야하지만 개발 환경이라 infreeJ 아이디로 수취
        String nickname = userMapper.findNicknameByUserId(event.getUserId());
        String accommodationName = boardMapper.getAccommodationNameByBoardId(event.getBoardId());

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("https://picsum.photos/id/10/200/300")
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(nickname) // 리뷰 작성자 닉네임
                .message("[" + accommodationName + "]" + " 신규 리뷰")
                .typeName("Review")
                .build();

        try {
            // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성
            String detailsObject = objectMapper.writeValueAsString(detailsResponse);

            // INSERT의 parameterType 객체 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(recipientId)
                    .notiType("NOTI_REVIEW_CREATE")
                    .targetId(event.getBoardId()) // 이동할 페이지 PK
                    .details(detailsObject)
                    .build();

            // DB 저장 후 SSE 요청 메서드 호출
            notificationService.saveNotification(notificationRequest, detailsResponse);

        } catch (JsonProcessingException e) {
            log.error("handleCouponCreatedEvent 처리 중 오류 발생. event: {}", event, e);
//            throw new RuntimeException(e);
        }
    }


    /**
     * 댓글 작성 이벤트리스너 메서드
     *
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener
    private void handleCommentCreatedEvent(CommentCreatedEvent event) {

        String nickname = userMapper.findNicknameByUserId(event.getUserId()); // 댓글 작성자 닉네임
        CommentsDto commentsDto = commentsMapper.getOneByCommentId(event.getCommentId()); // 댓글 데이터
        long recipientId = boardMapper.getUserIdByBoardId(event.getBoardId()); // 댓글이 작성된 게시글 작성자의 PK

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("https://picsum.photos/id/10/200/300")
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(nickname) // 댓글 작성자 닉네임
                .message(commentsDto.getContent())
                .typeName("Comment")
                .build();

        try {
            // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성
            String detailsObject = objectMapper.writeValueAsString(detailsResponse);

            // INSERT의 parameterType 객체 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(recipientId)
                    .notiType("NOTI_COMMENT_CREATE")
                    .targetId(commentsDto.getBoardId()) // 이동할 페이지 PK
                    .details(detailsObject)
                    .build();

            // DB 저장 후 SSE 요청하는 메서드 호출
            notificationService.saveNotification(notificationRequest, detailsResponse);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 회원가입 이벤트리스너 메서드
     *
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener
    private void handleSignupEvent(SignupEvent event) {

        long recipientId = event.getUserId();
        String nickname = userMapper.findNicknameByUserId(recipientId);

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("https://picsum.photos/id/10/200/300")
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(nickname + "님!")
                .message("회원가입을 축하합니다!")
                .typeName("Signup")
                .build();

        try {
            // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성
            String detailsObject = objectMapper.writeValueAsString(detailsResponse);

            // INSERT의 parameterType 객체 구성
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .userId(recipientId)
                    .notiType("NOTI_SIGNUP")
                    .targetId(recipientId) // 이동할 페이지 PK
                    .details(detailsObject)
                    .build();

            // DB 저장 후 SSE 요청하는 메서드 호출
            notificationService.saveNotification(notificationRequest, detailsResponse);


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}

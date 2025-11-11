package com.staylog.staylog.domain.notification.listener;

import com.staylog.staylog.domain.board.dto.CommentsDto;
import com.staylog.staylog.domain.board.mapper.BoardMapper;
import com.staylog.staylog.domain.board.mapper.CommentsMapper;
import com.staylog.staylog.domain.booking.entity.AccommodationIdAndName;
import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.notification.dto.request.NotificationRequest;
import com.staylog.staylog.domain.notification.dto.response.DetailsResponse;
import com.staylog.staylog.domain.notification.service.NotificationService;
import com.staylog.staylog.domain.payment.entity.Payment;
import com.staylog.staylog.domain.payment.mapper.PaymentMapper;
import com.staylog.staylog.domain.user.mapper.UserMapper;
import com.staylog.staylog.global.annotation.CommonRetryable;
import com.staylog.staylog.global.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationEventListener {

    private final UserMapper userMapper;
    private final CommentsMapper commentsMapper;
    private final BoardMapper boardMapper;
    private final PaymentMapper paymentMapper;
    private final BookingMapper bookingMapper;
    private final NotificationService notificationService;



    /**
     * 쿠폰 발급 알림(쿠폰 발급 이벤트리스너)
     *
     * @param event 쿠폰 발급 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleCouponIssuanceNotification(CouponCreatedEvent event) {
        long recipientId = event.getUserId(); // 수신자 PK
        log.info("handleCouponIssuanceNotification 리스너 실행. recipientId: {}, couponId: {}", recipientId, event.getCouponId());

        // 이미지 가져오기
        String imageUrl = notificationService.getImageUrl("IMG_FROM_ICON", 2);

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("/images/" + imageUrl)
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title("쿠폰이 발급되었습니다!")
                .message("쿠폰함을 확인해주세요")
                .typeName("Coupon")
                .build();

        // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성 메서드
        String detailsObject = notificationService.detailsToJsonString(detailsResponse);

        // INSERT의 parameterType 객체 구성
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(recipientId)
                .notiType("NOTI_COUPON_CREATE")
                .targetId(recipientId) // 이동할 페이지 PK
                .details(detailsObject)
                .build();

        // DB 저장 후 SSE 요청하는 메서드 호출
        notificationService.saveNotification(notificationRequest, detailsResponse);
    }

    /**
     * 전체 쿠폰 발급 알림(전체 사용자 일괄 쿠폰 발급 이벤트리스너)
     *
     * @param event 쿠폰 발급 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleCouponAllIssuanceNotification(CouponCreatedAllEvent event) {
        log.info("handleCouponAllIssuanceNotification 리스너 실행.");

        // 이미지 가져오기
        String imageUrl = notificationService.getImageUrl("IMG_FROM_ICON", 2);

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("/images/" + imageUrl)
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title("쿠폰이 발급되었습니다!")
                .message("쿠폰함을 확인해주세요")
                .typeName("Coupon")
                .build();

        // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성 메서드
        String detailsObject = notificationService.detailsToJsonString(detailsResponse);

        // INSERT의 parameterType 객체 구성
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(null) // 모든 유저에게 저장하므로 현재 null
                .notiType("NOTI_COUPON_CREATE")
                .targetId(null) // 이동할 페이지 PK -> 현재는 null
                .details(detailsObject)
                .batchId(UUID.randomUUID().toString()) // 알림 일괄 PUSH를 위한 UUID 구성
                .build();

        // DB 저장 후 SSE 요청하는 메서드 호출
        notificationService.saveAllNotification(notificationRequest, detailsResponse);
    }


    /**
     * 예약 완료 알림(결제 승인 이벤트리스너)
     *
     * @param event 결제 승인 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handlePaymentConfirmNotification(PaymentConfirmEvent event) {
        Payment payment = paymentMapper.findPaymentById(event.getPaymentId());
        long recipientId = bookingMapper.findUserIdByBookingId(event.getBookingId()); // 수신자(예약자) PK

        log.info("handlePaymentConfirmNotification 리스너 실행. recipientId: {}, paymentId: {}", recipientId, event.getPaymentId());
        AccommodationIdAndName accommodationInfo = bookingMapper.findAccommodationIdAndNameByBookingId(event.getBookingId()); // 숙소명

//        ImageResponse imageResponse = imageService.getImagesByTarget(
//                "IMG_FROM_ACCOMMODATION", (long) accommodationInfo.get("accommodationId")
//        );
//        String imageUrl = imageResponse.getImages().get(0).getImageUrl(); // 숙소 메인이미지

        // 이미지 가져오기
        String imageUrl = notificationService.getImageUrl("IMG_FROM_ACCOMMODATION", accommodationInfo.getAccommodationId());

        OffsetDateTime approvedAt = payment.getApprovedAt(); // 결제 승인 시간

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("/images/" + imageUrl)
                .date(approvedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(accommodationInfo.getAccommodationName())
                .message("예약이 확정되었습니다.")
                .typeName("Reservation")
                .build();

        // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성 메서드
        String detailsObject = notificationService.detailsToJsonString(detailsResponse);

        // INSERT의 parameterType 객체 구성
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(recipientId)
                .notiType("NOTI_RES_CONFIRM")
                .targetId(event.getPaymentId()) // 이동할 페이지 PK
                .details(detailsObject)
                .build();

        // DB 저장 후 SSE 요청하는 메서드 호출
        notificationService.saveNotification(notificationRequest, detailsResponse);

    }


    /**
     * 예약 취소 알림(결제 취소 이벤트리스너)
     *
     * @param event 결제 취소 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleRefundConfirmNotification(RefundConfirmEvent event) {
        long recipientId = bookingMapper.findUserIdByBookingId(event.getBookingId()); // 수신자(예약자) PK
        log.info("handleRefundConfirmNotification 리스너 실행. recipientId: {}, refundId: {}", recipientId, event.getRefundId());

        AccommodationIdAndName accommodationInfo = bookingMapper.findAccommodationIdAndNameByBookingId(event.getBookingId()); // 숙소명

        // 이미지 가져오기
        String imageUrl = notificationService.getImageUrl("IMG_FROM_ACCOMMODATION", accommodationInfo.getAccommodationId());

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("/images/" + imageUrl)
                .date(OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(accommodationInfo.getAccommodationName())
                .message("예약이 취소되었습니다.")
                .typeName("Reservation")
                .build();

        // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성 메서드
        String detailsObject = notificationService.detailsToJsonString(detailsResponse);

        // INSERT의 parameterType 객체 구성
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(recipientId)
                .notiType("NOTI_RES_CANCEL")
                .targetId(event.getRefundId()) // 이동할 페이지 PK
                .details(detailsObject)
                .build();

        // DB 저장 후 SSE 요청하는 메서드 호출
        notificationService.saveNotification(notificationRequest, detailsResponse);

    }


    /**
     * 리뷰글 작성 알림(리뷰 게시글 작성 이벤트리스너)
     *
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleReviewCreationNotification(ReviewCreatedEvent event) {
        long recipientId = (long) 7;  // TODO: 원래 Admin에게 보내야하지만 개발 환경이라 infreeJ 아이디로 수취
        log.info("handleReviewCreationNotification 리스너 실행. recipientId: {}, boardId: {}", recipientId, event.getBoardId());

        String nickname = userMapper.findNicknameByUserId(event.getUserId());
        AccommodationIdAndName accommodationInfo = boardMapper.getAccommodationNameAndIdByBoardId(event.getBoardId());

        // 이미지 가져오기
        String imageUrl = notificationService.getImageUrl("IMG_FROM_ACCOMMODATION", accommodationInfo.getAccommodationId());

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("/images/" + imageUrl)
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(nickname) // 리뷰 작성자 닉네임
                .message("[" + accommodationInfo.getAccommodationName() + "]" + " 신규 리뷰")
                .typeName("Review")
                .build();

        // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성 메서드
        String detailsObject = notificationService.detailsToJsonString(detailsResponse);

        // INSERT의 parameterType 객체 구성
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(recipientId)
                .notiType("NOTI_REVIEW_CREATE")
                .targetId(event.getBoardId()) // 이동할 페이지 PK
                .details(detailsObject)
                .build();

        // DB 저장 후 SSE 요청 메서드 호출
        notificationService.saveNotification(notificationRequest, detailsResponse);

    }


    /**
     * 댓글 작성 알림(댓글 작성 이벤트리스너)
     *
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleCommentCreationNotification(CommentCreatedEvent event) {
        long recipientId = boardMapper.getUserIdByBoardId(event.getBoardId()); // 댓글이 작성된 게시글 작성자의 PK
        log.info("handleCommentCreationNotification 리스너 실행. recipientId: {}, commentId: {}", recipientId, event.getCommentId());

        String nickname = userMapper.findNicknameByUserId(event.getUserId()); // 댓글 작성자 닉네임
        CommentsDto commentsDto = commentsMapper.getOneByCommentId(event.getCommentId()); // 댓글 데이터

        // 이미지 가져오기
        String imageUrl = notificationService.getImageUrl("IMG_FROM_PROFILE", event.getUserId());

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("/images/" + imageUrl)
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(nickname) // 댓글 작성자 닉네임
                .message(commentsDto.getContent())
                .typeName("Comment")
                .build();

        // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성 메서드
        String detailsObject = notificationService.detailsToJsonString(detailsResponse);

        // INSERT의 parameterType 객체 구성
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(recipientId)
                .notiType("NOTI_COMMENT_CREATE")
                .targetId(commentsDto.getBoardId()) // 이동할 페이지 PK
                .details(detailsObject)
                .build();

        // DB 저장 후 SSE 요청하는 메서드 호출
        notificationService.saveNotification(notificationRequest, detailsResponse);

    }


    /**
     * 회원가입 알림(회원가입 이벤트리스너)
     *
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleSignupNotification(SignupEvent event) {
        long recipientId = event.getUserId();
        log.info("handleSignupNotification 리스너 실행. recipientId: {}", recipientId);

        // 이미지 가져오기
        String imageUrl = notificationService.getImageUrl("IMG_FROM_ICON", 1);

        String nickname = userMapper.findNicknameByUserId(recipientId);

        // 알림 카드에 출력할 데이터 구성
        DetailsResponse detailsResponse = DetailsResponse.builder()
                .imageUrl("/images/" + imageUrl)
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .title(nickname + "님!")
                .message("회원가입을 축하합니다!")
                .typeName("Signup")
                .build();

        // 알림 데이터를 DB에 저장하기 위한 JSON 형태의 String 문자열 구성 메서드
        String detailsObject = notificationService.detailsToJsonString(detailsResponse);

        // INSERT의 parameterType 객체 구성
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(recipientId)
                .notiType("NOTI_SIGNUP")
                .targetId(recipientId) // 이동할 페이지 PK
                .details(detailsObject)
                .build();

        // DB 저장 후 SSE 요청하는 메서드 호출
        notificationService.saveNotification(notificationRequest, detailsResponse);
    }



    /**
     * Retryable 재시도 최종 실패 시 실행될 Recover 로직
     *
     * @param t     예외 객체
     * @param event 실패한 이벤트 객체
     * @author 이준혁
     */
    @Recover
    public void recoverNotifications(Throwable t, Object event) {
        log.error("[Recover] 알림 발송 재시도 최종 실패. 원인: {}", t.getMessage(), t);

        if (event instanceof CouponCreatedEvent cce) {
            log.error(" -> 실패 이벤트 상세: 쿠폰 발급 알림 (UserID: {})", cce.getUserId());

        } else if (event instanceof CouponCreatedAllEvent) {
            log.error(" -> 실패 이벤트 상세: 전체 쿠폰 발급 알림 (전체 발송)");

        } else if (event instanceof PaymentConfirmEvent pce) {
            log.error(" -> 실패 이벤트 상세: 예약 확정 알림 (PaymentID: {}, BookingID: {}, CouponID: {})",
                    pce.getPaymentId(), pce.getBookingId(), pce.getCouponId());

        } else if (event instanceof RefundConfirmEvent pce) {
            log.error(" -> 실패 이벤트 상세: 예약 취소 알림 (PaymentID: {}, BookingID: {}, RefundId: {})",
                    pce.getPaymentId(), pce.getBookingId(), pce.getRefundId());

        } else if (event instanceof ReviewCreatedEvent rce) {
            log.error(" -> 실패 이벤트 상세: 리뷰글 작성 알림 (UserID: {}, BoardID: {})",
                    rce.getUserId(), rce.getBoardId());

        } else if (event instanceof CommentCreatedEvent cce) {
            log.error(" -> 실패 이벤트 상세: 댓글 작성 알림 (UserID: {}, BoardID: {}, CommentID: {})",
                    cce.getUserId(), cce.getBoardId(), cce.getCommentId());

        } else if (event instanceof SignupEvent se) {
            log.error(" -> 실패 이벤트 상세: 회원가입 알림 (UserID: {})", se.getUserId());

        } else {
            // 향후 추가될 알림 관련 리스너를 위한 폴백
            log.error(" -> 실패 이벤트 상세: 알 수 없는 Event Type={}, Data={}",
                    event.getClass().getSimpleName(), event);
        }

        // 에러 테이블 추가 시 DB 저장 필요
    }

}

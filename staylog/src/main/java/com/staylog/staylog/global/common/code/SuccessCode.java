package com.staylog.staylog.global.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 성공 코드 정의
 * 모든 성공 메시지 코드를 중앙 집중식으로 관리
 *
 * @author 임채호
 */
@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // ==================== 공통 성공 (0xxx) ====================
    SUCCESS(200, "S0001", "success.common"),
    CREATED(201, "S0002", "success.created"),
    UPDATED(200, "S0003", "success.updated"),
    DELETED(200, "S0004", "success.deleted"),

    // ==================== 인증/인가 성공 (1xxx) ====================
    LOGIN_SUCCESS(200, "S1001", "success.auth.login"),
    LOGOUT_SUCCESS(200, "S1002", "success.auth.logout"),
    TOKEN_REFRESHED(200, "S1003", "success.auth.token.refreshed"),
    SIGNUP_SUCCESS(201, "S1004", "success.auth.signup"),

    // ==================== 이메일 인증 성공 (3xxx) ====================
    MAIL_SENT(200, "S3001", "success.mail.sent"),
    MAIL_VERIFIED(200, "S3002", "success.mail.verified"),

    // ==================== 회원 관련 성공 (2xxx) ====================
    USER_PROFILE_UPDATED(200, "S2001", "success.user.profile.updated"),
    PASSWORD_CHANGED(200, "S2002", "success.user.password.changed"),
    USER_NICKNAME_CHECKED(200, "S2003", "success.user.nickname.checked"),
    USER_LOGINID_CHECKED(200, "S2004", "success.user.loginId.checked"),
    USER_EMAIL_CHECKED(200, "S2005", "success.user.email.checked"),

    // ==================== 숙소 관련 성공 (4xxx) ====================
    ACCOMMODATION_CREATED(201, "S4001", "success.accommodation.created"),
    ACCOMMODATION_UPDATED(200, "S4002", "success.accommodation.updated"),
    ACCOMMODATION_FOUND(200, "S4003", "success.accommodation.found"),
    ACCOMMODATION_LIST_FOUND(200, "S4004", "success.accommodation.list.found"),

    // ==================== 예약 관련 성공 (5xxx) ====================
    BOOKING_CREATED(201, "S5001", "success.booking.created"),
    BOOKING_CANCELLED(200, "S5002", "success.booking.cancelled"),

    // ==================== 결제 관련 성공 (6xxx) ====================
    PAYMENT_PREPARED(200, "S6001", "success.payment.prepared"),
    PAYMENT_COMPLETED(200, "S6002", "success.payment.completed"),
    REFUND_COMPLETED(200, "S6003", "success.payment.refunded"),

    // ==================== 게시판 관련 성공 (8xxx) ====================
    BOARD_LIST_FETCHED(200, "S8004", "success.board.list"),
    BOARD_DETAIL_FETCHED(200, "S8005", "success.board.detail"),
    BOARD_CREATED(201, "S8001", "success.board.created"),
    BOARD_UPDATED(200, "S8002", "success.board.updated"),
    BOARD_DELETED(200, "S8003", "success.board.deleted"),

    // ==================== 리뷰 관련 성공 (7xxx) ====================
    REVIEW_CREATED(201, "S7001", "success.review.created"),
    REVIEW_UPDATED(200, "S7002", "success.review.updated"),
    REVIEW_DELETED(200, "S7003", "success.review.deleted"),

    // ==================== 저널 관련 성공 (8xxx) ====================
    JOURNAL_CREATED(201, "S8001", "success.journal.created"),
    JOURNAL_UPDATED(200, "S8002", "success.journal.updated"),
    JOURNAL_DELETED(200, "S8003", "success.journal.deleted");

    private final int httpStatus;
    private final String code;
    private final String messageKey;
}

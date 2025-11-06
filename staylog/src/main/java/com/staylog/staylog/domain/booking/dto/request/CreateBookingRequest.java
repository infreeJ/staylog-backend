package com.staylog.staylog.domain.booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 예약 생성 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    @NotNull(message = "객실 ID는 필수입니다")
    private Long roomId;

    @NotNull(message = "체크인 날짜는 필수입니다")
    @Future(message = "체크인은 미래 날짜여야 합니다")
    private LocalDate checkIn;

    @NotNull(message = "체크아웃 날짜는 필수입니다")
    private LocalDate checkOut;

    @NotNull(message = "결제 금액은 필수입니다")
    @Positive(message = "결제 금액은 양수여야 합니다")
    private Long amount;

    // 결제 수단 (선택) - null이면 기본값 5분 만료
    // "CARD", "VIRTUAL_ACCOUNT", "TRANSFER"
    @Pattern(regexp = "CARD|VIRTUAL_ACCOUNT|TRANSFER", message = "올바른 결제 수단이 아닙니다")
    private String paymentMethod;

    // 인원 정보 (선택)
    // guestName은 JWT의 nickname에서 자동으로 설정됩니다
    @Min(value = 1, message = "성인은 최소 1명 이상이어야 합니다")
    private Integer adults;

    @Min(value = 0, message = "어린이는 0명 이상이어야 합니다")
    private Integer children;

    @Min(value = 0, message = "유아는 0명 이상이어야 합니다")
    private Integer infants;
}

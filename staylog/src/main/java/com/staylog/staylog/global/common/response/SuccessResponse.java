package com.staylog.staylog.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 성공 응답 전용 클래스
 * ApiResponse<T>의 래퍼로, 성공 응답만 처리
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse<T> {

    /**
     * 성공 메시지
     */
    private String message;

    /**
     * 응답 데이터
     */
    private T data;

    /**
     * 응답 생성 시간
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 데이터 포함 성공 응답 생성
     */
    public static <T> SuccessResponse<T> of(String message, T data) {
        return SuccessResponse.<T>builder()
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 데이터 없는 성공 응답 생성
     */
    public static <T> SuccessResponse<T> of(String message) {
        return SuccessResponse.<T>builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * ApiResponse로 변환하기
     */
    public ApiResponse<T> toApiResponse() {
        return ApiResponse.<T>builder()
                .status("success")
                .message(this.message)
                .data(this.data)
                .timestamp(this.timestamp)
                .build();
    }
}

package com.staylog.staylog.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 통일된 API 응답 래퍼 클래스
 * 모든 API 응답을 일관된 형식으로 제공
 *
 * @param <T> 응답 데이터 타입
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * 응답 상태 (success, error, fail)
     */
    private String status;

    /**
     * 응답 메시지
     */
    private String message;

    /**
     * 응답 데이터 (제네릭 타입)
     */
    private T data;

    /**
     * 응답 생성 시간
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 성공 응답 생성 (데이터 포함)
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 성공 응답 생성 (데이터 없음)
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 에러 응답 생성
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 에러 응답 생성 (데이터 포함 - 에러 상세 정보)
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 실패 응답 생성 (클라이언트 입력 오류 등)
     */
    public static <T> ApiResponse<T> fail(String message, T data) {
        return ApiResponse.<T>builder()
                .status("fail")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

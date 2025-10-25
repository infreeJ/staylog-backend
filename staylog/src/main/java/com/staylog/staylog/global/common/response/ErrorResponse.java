package com.staylog.staylog.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 에러 응답 전용 클래스
 * GlobalExceptionHandler에서 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * 에러 코드 (예: INVALID_CREDENTIALS, ACCOUNT_DISABLED)
     */
    private String errorCode;

    /**
     * 사용자에게 표시할 에러 메시지
     */
    private String message;

    /**
     * HTTP 상태 코드
     */
    private Integer status;

    /**
     * 에러가 발생한 API 경로
     */
    private String path;

    /**
     * HTTP 메서드
     */
    private String method;

    /**
     * 에러 발생 시간
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 유효성 검증 실패 시 상세 에러 목록
     */
    private List<FieldError> errors;

    /**
     * 추가 에러 상세 정보 (디버깅용)
     */
    private Object details;

    /**
     * 필드별 에러 정보
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        /**
         * 에러가 발생한 필드명
         */
        private String field;

        /**
         * 에러 메시지
         */
        private String message;

        /**
         * 거부된 값 (민감 정보는 마스킹 필요)
         */
        private Object rejectedValue;
    }

    /**
     * 기본 에러 응답 생성
     */
    public static ErrorResponse of(String errorCode, String message, Integer status, String path) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 유효성 검증 에러 응답 생성
     */
    public static ErrorResponse validationError(String message, List<FieldError> errors, String path) {
        return ErrorResponse.builder()
                .errorCode("VALIDATION_FAILED")
                .message(message)
                .status(400)
                .path(path)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * ApiResponse로 변환
     */
    public ApiResponse<ErrorResponse> toApiResponse() {
        return ApiResponse.<ErrorResponse>builder()
                .status("error")
                .message(this.message)
                .data(this)
                .timestamp(this.timestamp)
                .build();
    }
}

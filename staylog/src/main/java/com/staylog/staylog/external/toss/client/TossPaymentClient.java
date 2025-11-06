package com.staylog.staylog.external.toss.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staylog.staylog.external.toss.config.TossPaymentsConfig;
import com.staylog.staylog.external.toss.dto.request.TossCancelRequest;
import com.staylog.staylog.external.toss.dto.request.TossConfirmRequest;
import com.staylog.staylog.external.toss.dto.response.TossErrorResponse;
import com.staylog.staylog.external.toss.dto.response.TossPaymentResponse;
import com.staylog.staylog.global.exception.custom.payment.TossApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * 토스 페이먼츠 API 클라이언트
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TossPaymentClient {

    private final RestTemplate restTemplate;
    private final TossPaymentsConfig tossConfig;
    private final ObjectMapper objectMapper;

    /**
     * 결제 승인
     */
    public TossPaymentResponse confirm(TossConfirmRequest request) {
        String url = tossConfig.getApiUrl() + "/confirm";

        HttpHeaders headers = createHeaders();

        //멱등키 추가하기 (결제용)
        headers.set("Idemtpotency-Key", generatePaymentIdempotencyKey(request.getPaymentKey()));

        HttpEntity<TossConfirmRequest> entity = new HttpEntity<>(request, headers);

        try {
            log.info("토스 결제 승인 요청: paymentKey={}, orderId={}, amount={}",
                     request.getPaymentKey(), request.getOrderId(), request.getAmount());

            ResponseEntity<TossPaymentResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                TossPaymentResponse.class
            );

            log.info("토스 결제 승인 성공: paymentKey={}", request.getPaymentKey());
            return response.getBody();

        } catch (HttpStatusCodeException e) {
            log.error("토스 결제 승인 실패: statusCode={}, body={}",
                      e.getStatusCode(), e.getResponseBodyAsString());
            throw parseTossError(e);
        }
    }

    /**
     * 결제 취소/환불
     */
    public TossPaymentResponse cancel(String paymentKey, TossCancelRequest request) {
        String url = tossConfig.getApiUrl() + "/" + paymentKey + "/cancel";

        HttpHeaders headers = createHeaders();

        // 멱등키 추가하기 (중복 환불 방지)
        headers.set("Idempotency-Key", generateCancelIdempotencyKey(paymentKey));

        HttpEntity<TossCancelRequest> entity = new HttpEntity<>(request, headers);

        try {
            log.info("토스 결제 취소 요청: paymentKey={}, cancelAmount={}",
                     paymentKey, request.getCancelAmount());

            ResponseEntity<TossPaymentResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                TossPaymentResponse.class
            );

            log.info("토스 결제 취소 성공: paymentKey={}", paymentKey);
            return response.getBody();

        } catch (HttpStatusCodeException e) {
            log.error("토스 결제 취소 실패: statusCode={}, body={}",
                      e.getStatusCode(), e.getResponseBodyAsString());
            throw parseTossError(e);
        }
    }

    /**
     * 결제 조회
     */
    public TossPaymentResponse getPayment(String paymentKey) {
        String url = tossConfig.getApiUrl() + "/" + paymentKey;

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TossPaymentResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                TossPaymentResponse.class
            );

            return response.getBody();

        } catch (HttpStatusCodeException e) {
            throw parseTossError(e);
        }
    }

    /**
     * HTTP 헤더 생성
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + tossConfig.getEncodedSecretKey());
        return headers;
    }

    /**
     * 결제 멱등키 생성
     */
    private String generatePaymentIdempotencyKey(String paymentKey) {
        return "CONFIRM_" + paymentKey;
    }

    /**
     * 환불(취소) 멱등키 생성
     */
    private String generateCancelIdempotencyKey(String refundId) {
        return "CANCEL_" + refundId;
    }

    /**
     * 토스 에러 파싱
     */
    private TossApiException parseTossError(HttpStatusCodeException e) {
        try {
            TossErrorResponse error = objectMapper.readValue(
                e.getResponseBodyAsString(),
                TossErrorResponse.class
            );
            return new TossApiException(error.getCode(), error.getMessage());
        } catch (Exception ex) {
            return new TossApiException("UNKNOWN", e.getMessage());
        }
    }
}

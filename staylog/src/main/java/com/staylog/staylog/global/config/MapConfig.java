package com.staylog.staylog.global.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration //정나영
public class MapConfig {
	/*
	 * 카카오 api 서버로 HTTP 요청을 보낼 클라이언트를 스프링에 등록
	 * */

	@Bean
    public WebClient kakaoWebClient(
            @Value("${kakao.local.base-url}") String baseUrl, //@Value("${...}"): application-private-dev.yml 에서 설정값을 읽어옴.
            @Value("${kakao.local.rest-api-key}") String apiKey) {

        return WebClient.builder()
                .baseUrl(baseUrl) //카카오 api 서버 기본 주소
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiKey) //모든 요청에 자동으로 Authorization: KakaoAK {REST_API_KEY} 헤더를 넣음.
                .build();
    }
}

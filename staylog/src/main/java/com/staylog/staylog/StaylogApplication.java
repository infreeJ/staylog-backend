package com.staylog.staylog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@SpringBootApplication
@EnableScheduling // 스케줄러
public class StaylogApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaylogApplication.class, args);
	}
}

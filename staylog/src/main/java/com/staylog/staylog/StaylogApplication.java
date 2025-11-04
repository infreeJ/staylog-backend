package com.staylog.staylog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄러
public class StaylogApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaylogApplication.class, args);
	}
}

package com.staylog.staylog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ApiControllerTest {
	
	@GetMapping("/test")
	public String hello() {
		System.out.println("프론트에서 test 요청 들어옴");
		return "api 연결 성공";
	}
}
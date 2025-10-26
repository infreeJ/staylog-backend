package com.staylog.staylog.domain.admin.accommodation.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminAccommodationController {
    private String role;
    private String status;
    private String from;
    private String to;
    private Integer limit;
    private Integer offset;
}

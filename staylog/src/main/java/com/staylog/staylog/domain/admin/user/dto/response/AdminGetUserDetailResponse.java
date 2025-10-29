package com.staylog.staylog.domain.admin.user.dto.response;

import com.staylog.staylog.domain.user.dto.UserDto;
import lombok.Data;

import java.util.List;

/**
 * 관리자 유저 목록 및 상세 조회 응답 Dto
 */
@Data
public class AdminGetUserDetailResponse {
    // 목록 조회용 필드
    private int totalCount;
    private List<UserDto> users;
    private int prevNum;
    private int nextNum;
    private int startPageNum;
    private int endPageNum;
    private int totalPageCount;
    // 유저 상세 조회 용 (단일 유저만)
    private UserDto user;


}

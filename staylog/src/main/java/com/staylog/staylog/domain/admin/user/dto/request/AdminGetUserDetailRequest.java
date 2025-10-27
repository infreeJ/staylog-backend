package com.staylog.staylog.domain.admin.user.dto.request;

import lombok.Data;

public class AdminGetUserDetailRequest {
    @Data
    public class AdminUserSearchReq {
        private String role; // 권한
        private String status; // 상태
        // 페이징 처리를 위한 필드
        private int startRowNum;
        private int endRowNum;
        // 이전글, 다음글 처리를 위한 필드
        private int prevNum;
        private int nextNum;


//        private String from; // 날짜 관련 시작일
//        private String to; // 날짜 관련 종료일
//        private Integer limit; // 한 페이지 당 개수
//        private Integer offset; // 현재 몇 번째부터 가져왔는지
    }

}
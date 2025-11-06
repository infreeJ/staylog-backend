package com.staylog.staylog.domain.admin.user.dto.response;

import com.staylog.staylog.domain.admin.user.dto.AdminUserListDto;
import com.staylog.staylog.global.common.response.PageResponse;
import lombok.*;

import java.util.List;

/**
 * 관리자 유저 목록 조회용
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUserListResponse {
    // 목록 조회용 필드
    private List<AdminUserListDto> users;
    // 페이징 처리용
    private PageResponse page;
}

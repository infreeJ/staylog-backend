package com.staylog.staylog.domain.admin.user.mapper;

import com.staylog.staylog.domain.admin.user.dto.request.AdminGetUserDetailRequest;
import com.staylog.staylog.domain.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminUserMapper {
    /**
     * 유저 목록 조회
     *
     * @param req 검색 조건, 페이지 정보 담은 요청 객체
     * @Date 2025.10.27
     * @Return 유저 목록
     */
    List<UserDto> findUsers(AdminGetUserDetailRequest.AdminUserSearchReq req);
}

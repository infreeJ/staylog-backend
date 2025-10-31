package com.staylog.staylog.domain.admin.user.mapper;

import com.staylog.staylog.domain.admin.user.dto.AdminUserDetailDto;
import com.staylog.staylog.domain.admin.user.dto.AdminUserListDto;
import com.staylog.staylog.domain.admin.user.dto.request.AdminUserListRequest;
import com.staylog.staylog.domain.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 관리자 유저 관리용 MyBatis 매퍼
 */
@Mapper
public interface AdminUserMapper {
    /**
     * 유저 목록 조회
     *
     * @param req 검색 조건, 페이지 정보 담은 요청 객체
     * @return 유저 목록
     */
    List<AdminUserListDto> findUsers(AdminUserListRequest req);
    /**
     * 페이지네이션을 위한 유저 수
     * @param req 검색조건
     * @return 총 건수
     */
    int countUsers(AdminUserListRequest req);
    /**
     * 유저 상세 조회
     */
    AdminUserDetailDto getUserDetail(Long userId);
    /**
     * 유저 권한 변경 (role 변경)
     */
    int updateUserRole(@Param("userId") Long userId, @Param("role") String role);
    /**
     * 유저 정보 수정(상태)
     */
    int updateUserStatus(@Param("userId") Long userId, @Param("status") String status);
}

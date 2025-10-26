package com.staylog.staylog.domain.admin.user.mapper;

import com.staylog.staylog.domain.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminUserMapper {
    /**
     * 유저 목록 조회 매퍼
     */
    List<UserDto> findUsers();
}

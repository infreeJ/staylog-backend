package com.staylog.staylog.domain.user.repository;

import com.staylog.staylog.domain.user.dto.UserDto;
import com.staylog.staylog.domain.user.dto.request.SignupRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /**
     * 회원가입 매퍼
     * @author 이준혁
     * @param signupRequest
     * @return INSERT 성공 시 1 / 실패 시 0
     */
    public int signupUser(SignupRequest signupRequest);

    /**
     * 유저 정보 조회 매퍼
     * @author 이준혁
     * @param userId
     * @return UserDto
     */
    public UserDto getByUserId(String userId);
}

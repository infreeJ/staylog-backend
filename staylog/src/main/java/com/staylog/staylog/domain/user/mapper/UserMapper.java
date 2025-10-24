package com.staylog.staylog.domain.user.mapper;

import com.staylog.staylog.domain.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * User 도메인 MyBatis Mapper 인터페이스
 * 사용자 정보에 대한 데이터베이스 접근을 담당
 */
@Mapper
public interface UserMapper {

    /**
     * loginId로 사용자 조회 (로그인 시 사용)
     * @param loginId 로그인 ID
     * @return 사용자 정보 (없으면 null)
     */
    UserDto findByLoginId(@Param("loginId") String loginId);

    /**
     * userId로 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 정보 (Optional)
     */
    Optional<UserDto> findByUserId(@Param("userId") Long userId);

    /**
     * email로 사용자 조회 (중복 확인, 비밀번호 찾기 등에 사용)
     * @param email 이메일 주소
     * @return 사용자 정보 (Optional)
     */
    UserDto findByEmail(@Param("email") String email);


}

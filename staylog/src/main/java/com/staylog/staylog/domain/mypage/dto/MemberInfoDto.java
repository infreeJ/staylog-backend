package com.staylog.staylog.domain.mypage.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 마이페이지 회원정보 DTO (조회 + 수정 통합)
 * @Author 오미나
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberInfoDto {

    private Long userId;          // 회원 고유 ID (USER_ID(PK))
    private String loginId;       // 로그인 ID
    private String password;	  // 비밀번호 (원래 DB의 비밀번호 컬럼과 매핑되는 필드)
    private String email;         // 이메일
    private String role;          // 사용자 권한
    private String name;          // 이름
    private String nickname;      // 닉네임
    private String phone;         // 전화번호
    private String gender;        // 성별 GENDER (M/F)
    private String birthDate;     // 생년월일 BIRTH_DATE (YYYY-MM-DD)
    private String profileImage;  // 프로필 이미지 PROFILE_IMAGE
    private LocalDateTime createdAt; // 가입일
    private LocalDateTime updatedAt; // 수정일
}

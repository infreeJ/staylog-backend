package com.staylog.staylog.domain.mypage.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoResponse {
    private Long userId;  //회원 고유 ID
    private String loginId; //로그인 ID
    private String email; //이메일
    private String role; //사용자 권한(역할)
    private String name; //이름
    private String nickname;  //닉네임
    private String phone; //전화번호
    private String gender; //성별 (선택)
    private String birthDate; //생년월일 (선택)
    private String profileImage; //프로필 이미지 (선택)
    private LocalDateTime createdAt; //가입일
    private LocalDateTime updatedAt; //수정일
}





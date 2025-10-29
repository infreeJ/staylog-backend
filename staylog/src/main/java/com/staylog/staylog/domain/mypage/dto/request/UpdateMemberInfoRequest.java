package com.staylog.staylog.domain.mypage.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원정보 수정 요청 DTO
 * @Author 오미나
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberInfoRequest {

    private Long userId; //회원 고유 ID
    private String name; //이름
    private String nickname; //닉네임
    private String phone; //전화번호
    private String gender; //성별 (선택)
    private String birthDate; //생년월일 (선택)
    private String profileImage; //프로필 이미지 (선택)
}

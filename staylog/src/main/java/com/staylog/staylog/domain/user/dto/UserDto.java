package com.staylog.staylog.domain.user.dto;


import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Alias("userDto")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String loginId;
    private String password;
    private String nickname;
    private String name;
    private String email;
    private String phone;
    private String profileImage;
    private String birthDate;
    private String gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role;
    private String status;
    private LocalDateTime lastLogin;
    private LocalDateTime withdrawnAt;


}

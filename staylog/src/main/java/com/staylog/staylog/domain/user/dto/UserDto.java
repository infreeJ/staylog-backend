package com.staylog.staylog.domain.user.dto;

import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long userId;
    private String userName;
    private String userNickname;
    private String email;
    private String password;
    private String phone;
    private String profileImage;
    private LocalDateTime birthDate;
    private String gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userRole;
    private String status;
    private LocalDateTime lastLogin;
    private LocalDateTime withDrawnAt;
}

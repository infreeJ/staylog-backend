package com.staylog.staylog.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Alias("nicknameCheckedResponse")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NicknameCheckedResponse {
    private String nickname;
    private boolean isDuplicate;
}

package com.staylog.staylog.domain.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Alias("readAllRequest")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadAllRequest {
    private long userId;
}

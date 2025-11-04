package com.staylog.staylog.domain.admin.board.dto;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("BoardStatusRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminBoardStatusRequest {
	
    private Long boardId;
    
    /** 변환활 상태값 */
    private String deletedYn;
}

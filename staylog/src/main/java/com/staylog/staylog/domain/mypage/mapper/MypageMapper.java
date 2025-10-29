package com.staylog.staylog.domain.mypage.mapper;

import com.staylog.staylog.domain.mypage.dto.request.*;
import com.staylog.staylog.domain.mypage.dto.response.*;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MypageMapper {

    MemberInfoResponse selectMemberInfo(Long userId);
    int updateMemberInfo(UpdateMemberInfoRequest req);
    //int deleteMemberInfo(Long userId);


}


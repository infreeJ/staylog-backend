package com.staylog.staylog.domain.mypage.service;

import com.staylog.staylog.domain.mypage.dto.request.*;
import com.staylog.staylog.domain.mypage.dto.response.*;


public interface MypageService {
    MemberInfoResponse getMemberInfo(Long userId);
    void updateMemberInfo(UpdateMemberInfoRequest req);
    //void deleteMemberInfo(Long userId);

}


package com.staylog.staylog.domain.mypage.service.impl;

import com.staylog.staylog.domain.mypage.dto.request.*;
import com.staylog.staylog.domain.mypage.dto.response.*;
import com.staylog.staylog.domain.mypage.mapper.MypageMapper;
import com.staylog.staylog.domain.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MypageServiceImpl implements MypageService {

    private final MypageMapper mypageMapper;

    @Override
    public MemberInfoResponse getMemberInfo(Long userId) {
        log.info("회원 정보 조회: userId={}", userId);
        return mypageMapper.selectMemberInfo(userId);
    }

    @Override
    public void updateMemberInfo(UpdateMemberInfoRequest req) {
        log.info("회원 정보 수정: userId={}", req.getUserId());
        mypageMapper.updateMemberInfo(req);
    }

//    @Override
//    public void deleteMemberInfo(Long userId) {
//        log.info("회원 탈퇴 처리: userId={}", userId);
//        mypageMapper.deleteMemberInfo(userId);
//    }

 
}


package com.yeojeong.application.domain.member.application;

import com.yeojeong.application.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberChangeService {

    private final MemberService memberService;

    public void loginSuccessAndLastLoginDateChange(Long id) {
        Member member = memberService.findById(id);
        member.changeLastLoginDate();
        memberService.save(member);
    }

}

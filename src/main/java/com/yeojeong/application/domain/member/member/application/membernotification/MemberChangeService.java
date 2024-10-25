package com.yeojeong.application.domain.member.member.application.membernotification;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.member.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.domain.MemberRepository;
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

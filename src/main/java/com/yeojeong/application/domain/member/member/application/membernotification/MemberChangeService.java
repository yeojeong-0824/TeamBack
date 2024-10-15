package com.yeojeong.application.domain.member.member.application.membernotification;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberChangeService {

    private final MemberRepository memberRepository;
    public void loginSuccessAndLastLoginDateChange(Long takenMemberId) {
        Member member = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));
        member.changeLastLoginDate();
        memberRepository.save(member);
    }

}

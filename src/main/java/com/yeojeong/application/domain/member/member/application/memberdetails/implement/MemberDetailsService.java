package com.yeojeong.application.domain.member.member.application.memberdetails.implement;

import com.yeojeong.application.domain.member.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberDetails;
import com.yeojeong.application.domain.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberService memberService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.findByUsername(username);
        return new MemberDetails(member);
    }
}

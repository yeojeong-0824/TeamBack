package com.yeojeong.application.domain.member.application.memberdetails;

import com.yeojeong.application.domain.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
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

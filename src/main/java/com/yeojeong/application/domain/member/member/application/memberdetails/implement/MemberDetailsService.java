package com.yeojeong.application.domain.member.member.application.memberdetails.implement;

import com.yeojeong.application.config.exception.handler.ErrorCode;
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

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.NOT_FOUND_USER.getMessage()));

        return new MemberDetails(member);
    }
}

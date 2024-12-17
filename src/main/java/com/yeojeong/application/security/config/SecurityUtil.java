package com.yeojeong.application.security.config;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.domain.member.domain.MemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {}

    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof MemberDetails) {
            return ((MemberDetails) authentication.getPrincipal()).getMemberId();
        }
        throw new AuthedException("인증되지 않은 사용자 입니다.");
    }

    public static MemberDetails getCurrentMember(Authentication authResult) {
        try {
            return (MemberDetails) authResult.getPrincipal();
        } catch (Exception e){
            throw new AuthedException("access token 이 존재하지 않습니다.");
        }
    }
}

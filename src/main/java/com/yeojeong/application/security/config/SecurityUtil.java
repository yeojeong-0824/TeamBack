package com.yeojeong.application.security.config;

import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof MemberDetails) {
            return ((MemberDetails) authentication.getPrincipal()).getMemberId();
        }
        return null;
    }
}

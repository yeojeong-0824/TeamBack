package com.example.demo.config.util;

import com.example.demo.config.exception.ServerException;
import com.example.demo.member.member.presentation.dto.MemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof MemberDetails) {
            return ((MemberDetails) authentication.getPrincipal()).getMemberId();
        }
        // 인증이 필요하지 않은 메서드에서 해당 메서드를 실행하는 건 서버의 문제라고 생각해서 ServerException으로 처리했어요!
        throw new ServerException("인증되지 않은 사용자");
    }
}

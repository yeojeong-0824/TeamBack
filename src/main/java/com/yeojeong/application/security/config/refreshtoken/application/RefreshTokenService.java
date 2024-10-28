package com.yeojeong.application.security.config.refreshtoken.application;

import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;

public interface RefreshTokenService {
    void save(RefreshToken data);
    RefreshToken findById(String token);
    void delete(RefreshToken token);
    RefreshToken validRefresh(String token);
    String createRefresh(Long memberId, MemberDetails member);
}

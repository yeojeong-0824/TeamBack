package com.yeojeong.application.security.config.refreshtoken.application.refreshtokenfacade;


import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import jakarta.servlet.http.Cookie;

public interface RefreshTokenFacade {
    MemberDetails getMemberDetailsByRefreshToken(Cookie[] cookies);
    Cookie createNewRefreshTokenCookie(MemberDetails memberDetails);
    String createNewJwtToken(MemberDetails memberDetails);
}

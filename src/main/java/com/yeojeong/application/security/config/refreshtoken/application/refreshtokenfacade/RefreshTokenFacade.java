package com.yeojeong.application.security.config.refreshtoken.application.refreshtokenfacade;


import com.yeojeong.application.domain.member.domain.MemberDetails;
import jakarta.servlet.http.Cookie;

public interface RefreshTokenFacade {
    void remoteRefreshToken(Cookie[] cookies);
    MemberDetails getMemberDetailsByRefreshToken(Cookie[] cookies);
    Cookie createNewRefreshTokenCookie(MemberDetails memberDetails);
    String createNewJwtToken(MemberDetails memberDetails);
}

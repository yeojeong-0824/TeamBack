package com.yeojeong.application.security.config.refreshtoken.presentation;

import com.yeojeong.application.config.util.customannotation.MethodTimer;
import com.yeojeong.application.domain.member.member.application.memberfacade.MemberFacade;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberDetails;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.application.RefreshTokenService;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class RefreshController {
    private final RefreshTokenService refreshTokenService;
    private final MemberFacade memberFacade;
    private final JwtProvider jwtProvider;

    @MethodTimer(method = "리프레시 토큰 재발급")
    @Operation(summary = "리프레시 토큰 재발급")
    @PostMapping("/refresh")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "refresh token 발급 성공"),
                    @ApiResponse(responseCode = "400", description = "refresh token 발급 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Void> refresh(
            @CookieValue(value = "Refresh") Cookie cookie, HttpServletRequest request, HttpServletResponse response, Authentication authResult
    ){
        String refreshTokenHeader = cookie.getValue();

        // refresh token 이 유효한지 확인 -> 제거
        RefreshToken savedRefreshToken = refreshTokenService.validRefresh(refreshTokenHeader);
        refreshTokenService.delete(savedRefreshToken);

        MemberDetails member = (MemberDetails) authResult.getPrincipal();

        // refresh token 을 새롭게 생성
        String refreshToken = refreshTokenService.createRefresh(member.getMemberId(), member);

        Cookie refreshCookie = new Cookie(jwtProvider.REFRESH_HEADER_STRING, jwtProvider.TOKEN_PREFIX_REFRESH + refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(jwtProvider.REFRESH_EXPIRATION_TIME / 1000);

        String jwtToken = jwtProvider.createJwtToken(member);

        response.addHeader(jwtProvider.JWT_HEADER_STRING, jwtProvider.TOKEN_PREFIX_JWT + jwtToken);
        response.addCookie(refreshCookie);

        return null;
    }
}

package com.yeojeong.application.security.config.refreshtoken.presentation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yeojeong.application.config.util.customannotation.MethodTimer;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/token")
@Slf4j
@RequiredArgsConstructor
public class RefreshController {
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    @MethodTimer(method = "리프레시 토큰 재발급")
    @Operation(summary = "리프레시 토큰 재발급")
    @GetMapping("/refresh")
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

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @MethodTimer(method = "액세스 토큰 유효성 검사")
    @Operation(summary = "액세스 토큰 유효성 검사")
    @GetMapping("/access")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "access token 유효성 검사 성공"),
                    @ApiResponse(responseCode = "400", description = "access token 유효성 검사 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Boolean> access(
            HttpServletRequest request, HttpServletResponse response
    ){
        String accessToken = request.getHeader(jwtProvider.JWT_HEADER_STRING);
        if (accessToken == null) return null;//throw new RestApiException(ErrorCode.UNAUTHORIZED_CLIENT);

        String token = accessToken.replace(jwtProvider.TOKEN_PREFIX_JWT, "");

        Long userCode = null;
        try {
            userCode = JWT.require(Algorithm.HMAC512(jwtProvider.SECRET)).build().verify(token).getClaim("id").asLong();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}

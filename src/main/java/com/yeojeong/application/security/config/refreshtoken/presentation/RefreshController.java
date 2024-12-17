package com.yeojeong.application.security.config.refreshtoken.presentation;

import com.yeojeong.application.config.util.customannotation.MethodTimer;
import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.application.refreshtokenfacade.RefreshTokenFacade;
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
@RequestMapping("/tokens")
@Slf4j
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshTokenFacade refreshTokenFacade;

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
            HttpServletRequest request, HttpServletResponse response, Authentication authResult
    ){
        Cookie[] cookies = request.getCookies();

        MemberDetails memberDetails = refreshTokenFacade.getMemberDetailsByRefreshToken(cookies);
        Cookie refreshCookie = refreshTokenFacade.createNewRefreshTokenCookie(memberDetails);

        String jwtToken = refreshTokenFacade.createNewJwtToken(memberDetails);
        response.addHeader(JwtProvider.JWT_HEADER_STRING, JwtProvider.TOKEN_PREFIX_JWT + jwtToken);
        response.addCookie(refreshCookie);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @MethodTimer(method = "액세스 토큰 유효성 검사")
    @Operation(summary = "액세스 토큰 유효성 검사")
    @GetMapping("/access")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유효한 토큰"),
                    @ApiResponse(responseCode = "403", description = "유효하지 않은 토큰"),
            }
    )
    public ResponseEntity<Void> access() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

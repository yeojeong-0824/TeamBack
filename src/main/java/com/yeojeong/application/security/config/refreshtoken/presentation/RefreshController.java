package com.yeojeong.application.security.config.refreshtoken.presentation;

import com.yeojeong.application.domain.member.domain.MemberDetails;
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
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tokens")
@Slf4j
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshTokenFacade refreshTokenFacade;

    @Operation(summary = "리프레시 토큰 삭제")
    @DeleteMapping("/refresh")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "refresh token 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "refresh token 삭제 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Void> refreshDelete(
            HttpServletRequest request, HttpServletResponse response
    ){
        Cookie[] cookies = request.getCookies();
        refreshTokenFacade.remoteRefreshToken(cookies);
        Cookie cookie = new Cookie(JwtProvider.REFRESH_HEADER_STRING, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

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

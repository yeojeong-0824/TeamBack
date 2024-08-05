package com.example.demo.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.member.member.presentation.dto.MemberDetails;
import com.example.demo.member.member.domain.Member;
import com.example.demo.security.JwtProvider;
import com.example.demo.security.refreshToken.RefreshToken;
import com.example.demo.security.refreshToken.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /*
        1. Jwt 재발급
        Step 1: Refresh 토큰이 존재하는지 확인
        Step 2: Refresh 토큰이 존재한다면, Jwt 토큰의 재발급 시도로 알고 Jwt 토큰 재발급을 시도
        Step 3: 재발급의 실패 시(만료: 401, 변조: 400) 오류를 반환, 성공 시 인증에 성공
        Step 4: 필터 종료

        ToDo: refresh 토큰을 redis로 설정하는 걸 고려해봐야 할 것 같음
        */
        String refreshTokenHeader = request.getHeader(jwtProvider.REFRESH_HEADER_STRING);
        if(refreshTokenHeader != null) {
            log.info("JWT Token 재발급");
            refreshTokenHeader = refreshTokenHeader.replace(jwtProvider.TOKEN_PREFIX, "");

            Optional<RefreshToken> savedOptionalRefreshToken = refreshTokenService.findRefreshToken(refreshTokenHeader);
            if(savedOptionalRefreshToken.isEmpty()) {
                log.info("Refresh Token이 존재하지 않습니다");
                response.setStatus(401);
                return;
            }

            RefreshToken savedRefreshToken = savedOptionalRefreshToken.get();

            // Step: 1 (Token 재발급 횟수 제한)
            // Refresh Token의 발급 가능 횟수를 초과 했을 때
            // Refresh Token을 삭제하고 만료된 Token으로 재발급을 실패
            if(savedRefreshToken.getCount() < 0) {
                log.info("Refresh Token의 재발급 횟수가 초과되었습니다");
                refreshTokenService.dropRefreshToken(refreshTokenHeader);
                response.setStatus(401);
                return;
            }

            Member tokenMember = jwtProvider.decodeToken(refreshTokenHeader, String.valueOf(savedRefreshToken.getExpirationTime()));
            Member savedMember = Member.builder()
                    .username(savedRefreshToken.getUsername())
                    .nickname(savedRefreshToken.getNickname())
                    .age(savedRefreshToken.getAge())
                    .role(savedRefreshToken.getRole())
                    .build();

            // Step: 2 (Token 변조 검사)
            // Refresh Token의 정보와 데이터베이스의 저장된 정보와 다를 때
            // Refresh Token을 삭제하고 유효하지 않은 Token으로 재발급을 실패
            if(!Member.equalsMember(savedMember, tokenMember)) {
                log.info("Refresh Token이 유효하지 않습니다");
                refreshTokenService.dropRefreshToken(refreshTokenHeader);
                response.setStatus(401);
                return;
            }

            log.info("만료된 JWT Token 재발급 완료");
            MemberDetails reissueTokenMemberDetails = new MemberDetails(savedMember);
            String reissueToken = jwtProvider.createJwtToken(reissueTokenMemberDetails);
            response.addHeader(jwtProvider.JWT_HEADER_STRING, jwtProvider.TOKEN_PREFIX + reissueToken);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(reissueTokenMemberDetails, null, reissueTokenMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }

        /*
        Jwt 확인
        Step 1: Jwt 토큰이 존재하는지 확인 -> 없다면 필터 종료
        Step 2: Jwt 토큰을 복호화 실패시(재발급 횟수 초과: 401, 변조: 400) 오류를 반환, 성공시 인증에 성공
        Step 3: 필터 종료
         */
        String jwtTokenHeader = request.getHeader(jwtProvider.JWT_HEADER_STRING);
        if(jwtTokenHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtTokenHeader = jwtTokenHeader.replace(jwtProvider.TOKEN_PREFIX, "");

        try {
            Member jwtTokenMember = jwtProvider.decodeToken(jwtTokenHeader, jwtProvider.SECRET);
            MemberDetails jwtTokenMemberDetails = new MemberDetails(jwtTokenMember);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(jwtTokenMemberDetails, null, jwtTokenMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch(TokenExpiredException e) {
            response.setStatus(401);
            log.info("JWT Token 유효기간이 만료되었습니다.");

        } catch (JWTDecodeException e) {
            log.info("JWT Token 값이 잘못되었습니다");

        } catch (JWTVerificationException e) {
            log.info("JWT Token 인증이 실패하였습니다");

        }

        filterChain.doFilter(request, response);
    }
}

package com.example.demo.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.dto.member.MemberDetails;
import com.example.demo.entity.Member;
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

import javax.swing.text.html.Option;
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

            // Refresh Token 재발급시 테스트를 진행하지 않았음
            // 리팩토링 필요해 보임

            String refreshTokenHeader = request.getHeader(jwtProvider.REFRESH_HEADER_STRING);
            if(refreshTokenHeader == null) {
                log.info("Refresh Token이 존재하지 않습니다");
                return;
            }

            refreshTokenHeader = refreshTokenHeader.replace(jwtProvider.TOKEN_PREFIX, "");

            Optional<RefreshToken> savedOptionalRefreshToken = refreshTokenService.findRefreshToken(refreshTokenHeader);
            if(savedOptionalRefreshToken.isEmpty()) {
                log.info("Refresh Token이 잘못되었습니다");
                return;
            }

            RefreshToken savedRefreshToken = savedOptionalRefreshToken.get();

            // 해당 IP가 Refresh Token을 발급받은 IP와 다를 때
            // Refresh Token을 삭제하고 유효하지 않은 사용자로 인증을 실패
            if(!request.getRemoteAddr().equals(savedRefreshToken.getIp())) {
                log.info("유효하지 않은 사용자");
                refreshTokenService.dropRefreshToken(refreshTokenHeader);
                return;
            }

            // Refresh Token의 발급 횟수를 초과 했을 때
            // Refresh Token을 삭제하고 만료된 Token으로 인증을 실패
            if(savedRefreshToken.getCount() < 0) {
                log.info("Refresh Token이 만료되었습니다");
                refreshTokenService.dropRefreshToken(refreshTokenHeader);
                return;
            }

            Member tokenMember = jwtProvider.decodeToken(refreshTokenHeader, String.valueOf(savedRefreshToken.getExpirationTime()));

            Member savedMember = Member.builder()
                    .nickname(savedRefreshToken.getNickname())
                    .age(savedRefreshToken.getAge())
                    .role(savedRefreshToken.getRole())
                    .build();

            // Refresh Token의 값과 데이터베이스의 저장된 정보와 다를 때
            // Refresh Token을 삭제하고 유효하지 않은 Token으로 인증을 실패
            if(!Member.equalsMember(savedMember, tokenMember)) {
                log.info("Refresh Token이 유효하지 않습니다");
                refreshTokenService.dropRefreshToken(refreshTokenHeader);
                return;
            }

            log.info("만료된 JWT Token 재발급");
            MemberDetails reissueTokenMemberDetails = new MemberDetails(savedMember);
            String reissueToken = jwtProvider.createJwtToken(reissueTokenMemberDetails);
            response.addHeader(jwtProvider.JWT_HEADER_STRING, jwtProvider.TOKEN_PREFIX + reissueToken);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(reissueTokenMemberDetails, null, reissueTokenMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JWTDecodeException e) {
            System.out.println(e.getMessage());
            log.info("JWT Token 값이 잘못되었습니다");

        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            log.info("JWT Token 인증이 실패하였습니다");

        }

        filterChain.doFilter(request, response);
    }
}

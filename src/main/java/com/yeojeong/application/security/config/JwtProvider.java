package com.yeojeong.application.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yeojeong.application.domain.member.domain.MemberDetails;
import com.yeojeong.application.domain.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

public class JwtProvider {

    private JwtProvider() {}

    static public String SECRET;

    static public final int JWT_EXPIRATION_TIME = 10 * 60 * 1000;
    static public final int REFRESH_EXPIRATION_TIME = 20 * 24 * 60 * 60 * 1000;

    static public final String REFRESH_HEADER_STRING = "Refresh";
    static public final String TOKEN_PREFIX_JWT = "Bearer ";
    static public final String JWT_HEADER_STRING = "Authorization";

    static public String createJwtToken(MemberDetails member) {

        String role = member.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);

        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .withClaim("id", member.getMemberId())
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(SECRET));

    }

    static public String createRefreshToken(MemberDetails member, String createTime) {

        String role = member.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);

        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .withClaim("id", member.getMemberId())
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(createTime));

    }

    static public Member decodeToken(String token, String key) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(key))
                .build()
                .verify(token);

        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").toString().replace("\"", "");

        return Member.builder()
                .id(id)
                .role(role)
                .build();
    }
}

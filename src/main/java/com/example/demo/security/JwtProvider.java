package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.member.member.presentation.dto.MemberDetails;
import com.example.demo.member.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

@Configuration
public class JwtProvider {

    @Value("${JWTKey}")
    public String SECRET;

    //JWT Token는 하루의 유효기간을 가짐
    public final int JWT_EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    //Refresh Token는 보름의 유효기간을 가짐
    public final int REFRESH_EXPIRATION_TIME = 15 * 24 * 60 * 60 * 1000;

    //JWT Token의 재발급은 10번으로 제한
    public final Integer REFRESH_COUNT = 10;

    public final String TOKEN_PREFIX = "Bearer ";
    public final String JWT_HEADER_STRING = "Authorization";
    public final String REFRESH_HEADER_STRING = "Refresh";

    public String createJwtToken(MemberDetails member) {

        String role = member.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);

        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .withClaim("id", member.getMemberId())
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(SECRET));

    }

    public String createRefreshToken(MemberDetails member, long createTime) {

        String role = member.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);

        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .withClaim("id", member.getMemberId())
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(String.valueOf(createTime + REFRESH_EXPIRATION_TIME)));

    }

    public Member decodeToken(String token, String key) {
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

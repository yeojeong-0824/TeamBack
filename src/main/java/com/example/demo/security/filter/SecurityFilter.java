package com.example.demo.security.filter;

import com.example.demo.domain.member.member.application.membernotification.MemberChangeService;
import com.example.demo.domain.member.member.application.memberservice.MemberService;
import com.example.demo.security.JwtProvider;
import com.example.demo.security.refreshtoken.refreshtokenservice.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberChangeService memberChangeService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(auth -> auth.configurationSource(corsConfigurationSource()))
                .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(new JwtFilter(jwtProvider, refreshTokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager, jwtProvider, refreshTokenService, memberChangeService),
                        UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        // 백엔드와 프론트엔드의 통신을 원활하게 하기 위함.
        // 이 설정을 따로 해두지 않으면 프론트 쪽에서 쿠키가 넘어가지 않을 수 있음.
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);  // 쿠키 사용 허용
        //TODO : 나중에 아래 주소는 도메인 주소로 바꿔야 함.
        config.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000"));  // 허용할 URL
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));  // 허용할 Http Method
        config.setAllowedHeaders(List.of("*"));  // 허용할 Header
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
package com.yeojeong.application.security.filter;

import com.yeojeong.application.domain.member.member.application.membernotification.MemberChangeService;
import com.yeojeong.application.security.FilterExceptionHandler;
import com.yeojeong.application.security.JwtProvider;
import com.yeojeong.application.security.refreshtoken.refreshtokenservice.RefreshTokenService;
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
    private final FilterExceptionHandler filterExceptionHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(auth -> auth.configurationSource(corsConfigurationSource()))
                .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint(filterExceptionHandler))
                        .accessDeniedHandler(new JwtAccessDeniedHandler(filterExceptionHandler)))

                .addFilterBefore(new JwtFilter(jwtProvider, refreshTokenService, filterExceptionHandler), UsernamePasswordAuthenticationFilter.class)
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

        config.addAllowedOriginPattern(CorsConfiguration.ALL);
        config.addAllowedMethod(CorsConfiguration.ALL);
        config.addAllowedHeader(CorsConfiguration.ALL);

        config.setExposedHeaders(List.of(jwtProvider.REFRESH_HEADER_STRING, jwtProvider.JWT_HEADER_STRING));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
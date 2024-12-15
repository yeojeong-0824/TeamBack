package com.yeojeong.application.security.filter;

import com.yeojeong.application.domain.member.application.membernotification.MemberChangeService;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.application.RefreshTokenService;
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

                .addFilterAt(new LoginFilter(authenticationManager, jwtProvider, refreshTokenService, memberChangeService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtFilter(jwtProvider), LoginFilter.class)

                .exceptionHandling(auth -> auth
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/members/authed/**").authenticated()
                        .requestMatchers("/boards/authed/**").authenticated()
                        .requestMatchers("/boards/comments/authed/**").authenticated()
                        .requestMatchers("/tokens/access").authenticated()
                        .anyRequest().permitAll());

        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

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
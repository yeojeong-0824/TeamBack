package com.yeojeong.application.security.config.refreshtoken.application.implement;

import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshTokenRepository;
import com.yeojeong.application.security.config.refreshtoken.application.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void save(RefreshToken data) {
        refreshTokenRepository.save(data);
    }

    @Override
    public RefreshToken findById(String token) {
        Optional<RefreshToken> savedOptionalRefreshToken = refreshTokenRepository.findById(token);
        if(savedOptionalRefreshToken.isEmpty()) return null;

        RefreshToken savedRefreshToken = savedOptionalRefreshToken.get();

        return savedRefreshToken;
    }

    @Override
    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    @Override
    public RefreshToken validRefresh (String refreshTokenHeader) {
        RefreshToken savedRefreshToken = null;
        if(refreshTokenHeader != null) {
            refreshTokenHeader = refreshTokenHeader.replace(jwtProvider.TOKEN_PREFIX_REFRESH, "");

            savedRefreshToken = findById(refreshTokenHeader);

            if(savedRefreshToken == null) {
                return null;
//                throw new RestApiException(ErrorCode.REFRESH_TOKEN_NOT_VALID);
            }
        } else {
            return null;
//            throw new RestApiException(ErrorCode.NOT_FOUND_COOKIE_REFRESH);
        }
        return savedRefreshToken;
    }

    @Override
    public String createRefresh(Long memberId, MemberDetails member){

        long loginTime = System.currentTimeMillis();
        String refreshToken = jwtProvider.createRefreshToken(member, loginTime);

        RefreshToken saveToken = RefreshToken.builder()
                .id(refreshToken)
                .expirationTime(loginTime + jwtProvider.REFRESH_EXPIRATION_TIME)
                .ttl(jwtProvider.REFRESH_EXPIRATION_TIME / 1000)
                .build();

        refreshTokenRepository.save(saveToken);

        return refreshToken;
    }
}

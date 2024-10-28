package com.yeojeong.application.domain.member.application.emailauthservice.implement;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.domain.member.application.emailauthservice.EmailAuthService;
import com.yeojeong.application.domain.member.domain.EmailAuth;
import com.yeojeong.application.domain.member.domain.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuthServiceImpl implements EmailAuthService {
    private final EmailAuthRepository emailAuthRepository;

    public void save(EmailAuth emailAuth) {
        emailAuthRepository.save(emailAuth);
    }

    public EmailAuth findById(String email) {
        return emailAuthRepository.findById(email).orElseThrow(() -> new AuthedException("해당 이메일 정보를 찾을 수 없습니다."));
    }

    public boolean checkAuthKey(String email, String authKey) {
        EmailAuth entity = findById(email);
        return entity.getValue().equals(authKey);
    }

    public void delete(String email) {
        emailAuthRepository.deleteById(email);
    }
}

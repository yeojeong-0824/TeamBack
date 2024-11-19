package com.yeojeong.application.domain.member.application.emailauthservice;

import com.yeojeong.application.domain.member.domain.EmailAuth;

public interface EmailAuthService {
    void save(EmailAuth emailAuth);
    EmailAuth findById(String email);
    boolean checkAuthedKey(String email, String authKey);
    void delete(String email);
}

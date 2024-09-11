package com.example.demo.domain.member.email.application.memberemailservice;

import com.example.demo.domain.member.email.presentation.dto.SendEmail;

public interface MemberEmailService {
    void sendNotification(String email);
    void sendAuthedEmail(SendEmail.JoinEmail dto);
    boolean checkAuthedKey(String email, String authedKey);
    boolean checkAuthedEmail(String email);
    String createAuthedKey();
    void sendFindPassword(SendEmail.FindPassword dto);
    void sendFindUsername(SendEmail.FindUsername dto);
}

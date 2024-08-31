package com.example.demo.domain.member.email.application.emailsender;

import com.example.demo.domain.member.email.presentation.dto.SendEmail;

public interface EmailSender {
    void join(SendEmail.JoinEmail dto);
    void findPassword(SendEmail.FindPassword dto);
    void findUsername(SendEmail.FindUsername dto);
}

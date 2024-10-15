package com.yeojeong.application.domain.member.email.application.emailsender;

import com.yeojeong.application.domain.member.email.presentation.dto.SendEmail;

public interface EmailSender {
    void notificationMember(String email);
    void join(SendEmail.JoinEmail dto);
    void findPassword(SendEmail.FindPassword dto);
    void findUsername(SendEmail.FindUsername dto);
}

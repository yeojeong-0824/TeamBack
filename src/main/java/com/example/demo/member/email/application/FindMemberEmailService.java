package com.example.demo.member.email.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindMemberEmailService {

    private final EmailService emailService;

    // ToDo: 전송 실패 시 처리 안해줌
    public void sendNewPasswordEmail(String email, String password) {
        String title = "새로운 비밀번호 발급";
        String text = "새로운 비밀번호: " + password;

        emailService.sendEmail(email, title, text);
    }

    public void sendUsernameEmail(String email, String username) {
        String title = "아이디 찾기";
        String text = "아이디: " + username;

        emailService.sendEmail(email, title, text);
    }
}

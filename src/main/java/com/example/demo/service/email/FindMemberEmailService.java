package com.example.demo.service.email;

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

    public String createNewPassword() {
        String password = "";
        for(int i = 0; i < 4; i++) {
            password += (char) ((int) (Math.random() * 26) + 97);
        }
        for(int i = 0; i < 4; i++) {
            password += (int) (Math.random() * 10);
        }
        return password;
    }
}

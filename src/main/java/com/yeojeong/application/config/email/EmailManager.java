package com.yeojeong.application.config.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailManager {
    private final EmailSender emailSender;

    public void emailAuth(String email, String authKey) {
        Context context = emailSender.createContext(List.of(authKey));
        emailSender.send(context, "여정에 오신 것을 환영합니다.", email, "join");
    }

    public void findPassword(String email, String password) {
        Context context = emailSender.createContext(List.of(password));
        emailSender.send(context, "비밀번호 재발급", email, "findPassword");
    }

    public void findUsername(String email, String username) {
        Context context = emailSender.createContext(List.of(username));
        emailSender.send(context, "아이디 찾기", email, "findUsername");
    }
}

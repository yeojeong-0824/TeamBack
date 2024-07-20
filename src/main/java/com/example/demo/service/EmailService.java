package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private Map<String, String> emailAuthendKeyMap = new HashMap<>(); // 일단 MAP으로 구현했습니다!

    // ToDo: 이메일 전송 시 유효시간을 만들어야 함, 어떤 식으로 로직을 작성할지 고민을 해봐야 할 것 같음
    // ToDo: 회원가입 시 이메일 인증이 된 유저인지 아닌지 확인하는 로직이 필요함

    public void sendEmail(String email, String title, String text, String authedKey) {
        SimpleMailMessage emailForm = createEmailForm(email, title, text);

        try {
            javaMailSender.send(emailForm);
            emailAuthendKeyMap.put(email, authedKey);
            log.info("이메일 전송 성공 To.{}, Title. {}, Text. {}", email, title, text);
        } catch (RuntimeException e) {
            log.error("이메일 전송 실패 To.{}, Title. {}, Text. {}", email, title, text);
        }
    }

    public String createAuthedKey() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis()); // 시스템 시간을 기준으로 난수를 설정하는 부분의 Seed가 적당한지 모르겠음

        return String.valueOf(random.nextInt(10000));
    }

    public boolean checkAuthedKey(String email, String key) {
        String savedKey = emailAuthendKeyMap.get(email);
        if(!savedKey.equals(key)) return false;

        emailAuthendKeyMap.remove(email);
        return true;
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}

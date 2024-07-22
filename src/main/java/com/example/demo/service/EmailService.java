package com.example.demo.service;

import com.example.demo.config.exception.RequestDataException;
import com.example.demo.dto.member.MemberRequest.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private Map<String, String> emailAuthendKeyMap = new HashMap<>(); // 일단 MAP으로 구현했습니다!

    // ToDo: 이메일 전송 시 유효시간을 만들어야 함, 어떤 식으로 로직을 작성 할 지 고민을 해봐야 할 것 같음
    // ToDo: 회원가입 시 이메일 인증이 된 유저인지 아닌지 확인하는 로직이 필요함

    // 인증 이메일을 전송하면
    // 이메일과 인증 이메일의 Key 값을 저장
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

        int rInt = random.nextInt(10000);
        return String.format("%04d", rInt);
    }

    // 이메일 인증을 시도하면 유효한 Option인지 확인함
    // 입력된 이메일과 매칭되는 Key 값이 동일하다면 > 이메일 인증이 성공한다면
    // 이메일의 상태를 Option으로 변경 > 인증에 성공 후 같은 이메일로 다른 작업을 수행하는 것을 방지

    public boolean checkAuthedKey(String email, String key, String option) {

        if(!checkValidByOption(option)) throw new RequestDataException("option의 값이 잘못되었습니다");

        String savedKey = emailAuthendKeyMap.get(email);
        if(savedKey == null) throw new RequestDataException("잘못된 이메일에 대한 요청입니다.");
        if(!savedKey.equals(key)) return false;

        emailAuthendKeyMap.put(email, option);
        return true;
    }

    // 인증된 이메일을 확인 하면 유효한 Option인지 확인함
    // 입력된 이메일의 상태가 동일하다면 > 이메일의 상태가 다르다면 예외를 호출
    // 인증된 사용자를 반환
    public boolean checkAuthedEmail(String email, String option) {
        if(!checkValidByOption(option)) throw new RequestDataException("option의 값이 잘못되었습니다");

        String savedKey = emailAuthendKeyMap.get(email);
        if(savedKey == null) throw new RequestDataException("잘못된 이메일에 대한 요청입니다.");
        if(!savedKey.equals(option)) throw new RequestDataException("잘못된 요청입니다");

        emailAuthendKeyMap.remove(email);
        return true;
    }

    // 유효한 Option인지 확인하는 메서드
    // 유효한 Option일 때 true
    // 유효하지 않은 Option일 때 false
    private boolean checkValidByOption(String option) {
        List<String> validOptionList = List.of("join", "changePassword");
        return validOptionList.contains(option);
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}

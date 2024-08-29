package com.example.demo.member.email.application;

import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.member.email.domain.Email;
import com.example.demo.member.email.domain.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * 회원가입에 대한 Email 전송
 */
@Service
@RequiredArgsConstructor
public class JoinMemberEmailService {

    private final EmailService emailService;
    private final EmailRepository emailRepository;

    private final String AUTHED = "Authed";
    private final int VALID_TIME = 5 * 60;

    // ToDo: 전송 실패 시 처리 안해줌
    @Transactional
    public void sendAuthedEmail(String email, String authedKey) {
        String title = "회원가입 이메일 인증 코드";
        String text = "이메일 인증 코드: " + authedKey;

        Email entity = Email.builder()
                .id(email)
                .value(authedKey)
                .ttl(VALID_TIME)
                .build();
        emailRepository.save(entity);
        emailService.sendEmail(email, title, text);
    }

    // 회원가입에 인증 이메일의 Key 값과 해당 이메일에 발송 된 Key 값이 같다면 해당 이메일의 값을 Authed로 변경
    public boolean checkAuthedKey(String email, String authedKey) {
        Email savedEntity = emailRepository.findById(email).orElseThrow(() -> new NotFoundDataException("해당 이메일의 정보가 없습니다"));
        if(!savedEntity.getValue().equals(authedKey)) return false;

        savedEntity.authedEmail(AUTHED);
        emailRepository.save(savedEntity);
        return true;
    }

    // 회원가입 인증 이메일의 값이 Authed라면 인증된 사용자라는 것으로 간주함
    public boolean checkAuthedEmail(String email) {
        Email savedEntity = emailRepository.findById(email).orElseThrow(() -> new NotFoundDataException("해당 이메일의 정보가 없습니다"));
        if(!savedEntity.getValue().equals(AUTHED)) return false;

        emailRepository.deleteById(email);
        return true;
    }

    // 회원가입 인증 이메일을 전송 할 때 임의의 4자리 수를 만들고 이 값을 Map 자료구조에 저장
    public String createAuthedKey() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        int rInt = random.nextInt(10000);
        return String.format("%04d", rInt);
    }
}

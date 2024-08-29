package com.example.demo.member.email.application;

import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.member.email.EmailSender;
import com.example.demo.member.email.domain.Email;
import com.example.demo.member.email.domain.EmailRepository;
import com.example.demo.member.email.dto.SendEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * 회원가입에 대한 Email 전송
 */
@Service
@RequiredArgsConstructor
public class MemberEmailServiceImpl implements MemberEmailService {

    private final EmailSender emailSender;
    private final EmailRepository emailRepository;

    private final String AUTHED = "Authed";
    private final int VALID_TIME = 5 * 60;

    @Transactional
    @Override
    public void sendAuthedEmail(String email, String authedKey) {

        SendEmail.JoinEmail dto = SendEmail.JoinEmail.builder()
                .email(email)
                .title("여정에 오신 것을 환영합니다")
                .authedKey(authedKey)
                .build();

        Email entity = Email.builder()
                .id(email)
                .value(authedKey)
                .ttl(VALID_TIME)
                .build();

        emailRepository.save(entity);
        emailSender.joinSendEmail(dto);
    }

    @Override
    public void sendNewPasswordEmail(String email, String password) {
        String title = "새로운 비밀번호 발급";
        String text = "새로운 비밀번호: " + password;

        emailSender.sendEmail(email, title, text);
    }

    @Override
    public void sendUsernameEmail(String email, String username) {
        String title = "아이디 찾기";
        String text = "아이디: " + username;

        emailSender.sendEmail(email, title, text);
    }



    @Transactional
    @Override
    public boolean checkAuthedKey(String email, String authedKey) {
        Email savedEntity = emailRepository.findById(email).orElseThrow(() -> new NotFoundDataException("해당 이메일의 정보가 없습니다"));
        if(!savedEntity.getValue().equals(authedKey)) return false;

        savedEntity.authedEmail(AUTHED);
        emailRepository.save(savedEntity);
        return true;
    }

    @Transactional
    @Override
    public boolean checkAuthedEmail(String email) {
        Email savedEntity = emailRepository.findById(email).orElseThrow(() -> new NotFoundDataException("해당 이메일의 정보가 없습니다"));
        if(!savedEntity.getValue().equals(AUTHED)) return false;

        emailRepository.deleteById(email);
        return true;
    }

    @Override
    public String createAuthedKey() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        int rInt = random.nextInt(10000);
        return String.format("%04d", rInt);
    }
}

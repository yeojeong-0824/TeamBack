package com.yeojeong.application.domain.member.email.application.memberemailservice.implement;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.yeojeong.application.domain.member.email.application.emailsender.EmailSender;
import com.yeojeong.application.domain.member.email.domain.Email;
import com.yeojeong.application.domain.member.email.domain.EmailRepository;
import com.yeojeong.application.domain.member.email.application.memberemailservice.MemberEmailService;
import com.yeojeong.application.domain.member.email.presentation.dto.SendEmail;
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

    @Override
    public void sendNotification(String email) {
        emailSender.notificationMember(email);
    }

    @Transactional
    @Override
    public void sendAuthedEmail(SendEmail.JoinEmail dto) {
        Email entity = Email.builder()
                .id(dto.email())
                .value(dto.authedKey())
                .ttl(VALID_TIME)
                .build();

        emailRepository.save(entity);
        emailSender.join(dto);
    }

    @Override
    public void sendFindPassword(SendEmail.FindPassword dto) {
        emailSender.findPassword(dto);
    }

    @Override
    public void sendFindUsername(SendEmail.FindUsername dto) {
        emailSender.findUsername(dto);
    }



    @Transactional
    @Override
    public boolean checkAuthedKey(String email, String authedKey) {
        Email savedEntity = emailRepository.findById(email).orElseThrow(() -> new NotFoundDataException(ErrorCode.NOT_FOUND_EMAIL));
        if(!savedEntity.getValue().equals(authedKey)) return false;

        savedEntity.authedEmail(AUTHED);
        emailRepository.save(savedEntity);
        return true;
    }

    @Transactional
    @Override
    public boolean checkAuthedEmail(String email) {
        Email savedEntity = emailRepository.findById(email).orElseThrow(() -> new NotFoundDataException(ErrorCode.NOT_FOUND_EMAIL));
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

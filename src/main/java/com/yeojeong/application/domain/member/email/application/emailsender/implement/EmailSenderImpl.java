package com.yeojeong.application.domain.member.email.application.emailsender.implement;

import com.yeojeong.application.domain.member.email.application.emailsender.EmailSender;
import com.yeojeong.application.domain.member.email.presentation.dto.SendEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void notificationMember(String email) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("message", "5개월 간 미접속");

            helper.setSubject("5개월 이상 미접속 회원");
            helper.setTo(email);

            String content = templateEngine.process("notification", context);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void join(SendEmail.JoinEmail dto) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("key", dto.authedKey());

            helper.setSubject("여정에 오신 것을 환영합니다!");
            helper.setTo(dto.email());

            String content = templateEngine.process("join", context);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void findPassword(SendEmail.FindPassword dto) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("password", dto.password());

            helper.setSubject("새로운 비밀번호 발급");
            helper.setTo(dto.email());

            String content = templateEngine.process("findPassword", context);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void findUsername(SendEmail.FindUsername dto) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("username", dto.username());

            helper.setSubject("아이디 찾기");
            helper.setTo(dto.email());

            String content = templateEngine.process("findUsername", context);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

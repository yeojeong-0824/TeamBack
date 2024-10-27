package com.yeojeong.application.config.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public Context createContext(List<String> data) {
        Context context = new Context();
        for(int i = 0; i < data.size(); i++) {
            context.setVariable("data" + (i + 1), data.get(i));
        }
        return context;
    }

    public void send(Context context, String title, String email, String template) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setSubject(title);
            helper.setTo(email);

            String content = templateEngine.process(template, context);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }
}

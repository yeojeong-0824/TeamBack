package com.yeojeong.application.config.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

//test
@Configuration
public class EmailConfig {

    @Value("${EmailPassword}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {

        String host = "smtp.gmail.com";
        String username = "webwinter04@gmail.com";
        int port = 587;

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());

        return mailSender;

    }

    private Properties getMailProperties() {

        boolean auth = true;
        boolean starttlsEnable = true;
        boolean starttlsRequired = true;
        int connectionTimeout = 5000;
        int timeout = 5000;
        int writeTimeout = 5000;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        properties.put("mail.smtp.starttls.required", starttlsRequired);
        properties.put("mail.smtp.connectiontimeout", connectionTimeout);
        properties.put("mail.smtp.timeout", timeout);
        properties.put("mail.smtp.writetimeout", writeTimeout);

        return properties;

    }
}
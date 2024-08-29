package com.example.demo.member.email.application.memberemailservice;

public interface MemberEmailService {
    void sendAuthedEmail(String email, String authedKey);
    boolean checkAuthedKey(String email, String authedKey);
    boolean checkAuthedEmail(String email);
    String createAuthedKey();
    void sendNewPasswordEmail(String email, String password);
    void sendUsernameEmail(String email, String username);
}

package com.yeojeong.application.domain.member.email.presentation.dto;

import lombok.Builder;

public class SendEmail {
    @Builder
    public record notificationEmail(
            String email,
            String message
    ){}
    @Builder
    public record JoinEmail(
            String email,
            String authedKey
    ){}

    @Builder
    public record FindPassword(
            String email,
            String password
    ){
        public static FindPassword toSendEmail(String email, String password) {
            return SendEmail.FindPassword.builder()
                    .email(email)
                    .password(password)
                    .build();
        }
    }

    @Builder
    public record FindUsername(
            String email,
            String username
    ){}
}

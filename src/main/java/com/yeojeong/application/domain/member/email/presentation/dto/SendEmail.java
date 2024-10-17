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
    ){}

    @Builder
    public record FindUsername(
            String email,
            String username
    ){}
}

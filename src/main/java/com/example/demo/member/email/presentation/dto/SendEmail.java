package com.example.demo.member.email.presentation.dto;

import lombok.Builder;

public class SendEmail {
    @Builder
    public record JoinEmail(
            String email,
            String title,
            String authedKey
    ){}
}

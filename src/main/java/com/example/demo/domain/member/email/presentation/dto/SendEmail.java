package com.example.demo.domain.member.email.presentation.dto;

import lombok.Builder;

public class SendEmail {
    @Builder
    public record JoinEmail(
            String email,
            String title,
            String authedKey
    ){}
}

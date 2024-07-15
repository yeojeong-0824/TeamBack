package com.example.demo.dto.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;
}

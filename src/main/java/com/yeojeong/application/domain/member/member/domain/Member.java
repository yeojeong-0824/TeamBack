package com.yeojeong.application.domain.member.member.domain;

import com.yeojeong.application.domain.member.member.presentation.dto.MemberRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity @Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private LocalDate lastLoginDate;

    public void patchPassword(String password) {
        this.password = password;
    }

    public void patchMember(MemberRequest.PatchMember takenDto) {
        if(takenDto.nickname() != null) this.nickname = takenDto.nickname();
    }
    public void changeLastLoginDate() {
        this.lastLoginDate = LocalDate.now();
    }
}

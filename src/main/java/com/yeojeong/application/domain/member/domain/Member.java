package com.yeojeong.application.domain.member.domain;

import com.yeojeong.application.config.util.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Entity @Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Member extends BaseTime implements Serializable {
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

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateMember(Member entity) {
        if(entity.getNickname() != null && !entity.getNickname().isEmpty()) {
            this.nickname = entity.getNickname();
        }
        if(entity.getAge() != null && entity.getAge() > 0) {
            this.age = entity.getAge();
        }
    }
    public void changeLastLoginDate() {
        this.lastLoginDate = LocalDate.now();
    }
}

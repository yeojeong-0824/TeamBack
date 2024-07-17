package com.example.demo.entity;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


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
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String role;

//    실행 오류로 일단 주석처리 해놨습니다
//    @OneToMany(mappedBy = "member", orphanRemoval = true)
//    private List<Board> boards;

//    @OneToMany(mappedBy = "member", orphanRemoval = true)
//    private List<Comment> comments;

    // Refresh Token 발급 시 유효한 Token인지 확인하기 위한 메소드
    public static boolean equalsMember(Member m1, Member m2) {
        if(!Objects.equals(m1.getNickname(), m2.getNickname())) return false;
        if(!Objects.equals(m1.getAge(), m2.getAge())) return false;
        if(!Objects.equals(m1.getRole(), m2.getRole())) return false;
        return true;
    }
}

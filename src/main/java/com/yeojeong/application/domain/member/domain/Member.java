package com.yeojeong.application.domain.member.domain;

import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Planner> planners = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Location> locations = new ArrayList<>();

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

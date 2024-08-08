package com.example.demo.board.comment.domain;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.comment.presentation.dto.CommentRequest.*;
import com.example.demo.member.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Board board;

    @Builder
    public Comment(String body, Member member, Board board) {
        this.body = body;
        this.member = member;
        this.board = board;
    }

    public void update(CommentUpdateRequest request) {
        body = request.body();
    }
}

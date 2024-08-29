package com.example.demo.domain.board.comment.domain;

import com.example.demo.domain.board.board.domain.Board;
import com.example.demo.config.util.BaseTime;
import com.example.demo.domain.board.comment.presentation.dto.CommentRequest;
import com.example.demo.domain.member.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer score;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Board board;

    public void update(CommentRequest.Edit editDto) {
        score = editDto.score();
        comment = editDto.comment();
    }
}

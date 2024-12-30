package com.yeojeong.application.domain.board.comment.domain;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment extends BaseTime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer score;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Board board;

    public void update(Comment updateEntity) {
        score = updateEntity.getScore();
        comment = updateEntity.getComment();
    }
}

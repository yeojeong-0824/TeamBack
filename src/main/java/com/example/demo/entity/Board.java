package com.example.demo.entity;

import com.example.demo.dto.board.BoardUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private Integer view;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Integer satisfaction;

    @Column(nullable = false)
    private Integer likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name="member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments;

    @Column(nullable = false)
    private Integer commentCount;

    public void update(BoardUpdateRequest boardUpdateRequest){
        this.title = boardUpdateRequest.getTitle();
        this.body = boardUpdateRequest.getBody();
        this.satisfaction = boardUpdateRequest.getSatisfaction();
    }

    public void viewUp(){this.view += 1;}

    public void commentCountUp(){this.commentCount += 1;}

    public void commentCountDown(){this.commentCount -= 1;}

}

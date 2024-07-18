package com.example.demo.entity;

import com.example.demo.dto.board.BoardRequest.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private Integer commentCount;

    public void update(BoardSaveRequest request){
        this.title = request.title();
        this.body = request.body();
        this.satisfaction = request.satisfaction();
    }

    public void viewUp(){this.view += 1;}

    public void commentCountUp(){this.commentCount += 1;}

    public void commentCountDown(){this.commentCount -= 1;}

}

package com.example.demo.board.board.domain;

import com.example.demo.board.board.presentation.dto.BoardRequest;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.config.entity.BaseTime;
import com.example.demo.member.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private String formattedAddress;

    // 경도
    @Column(nullable = false)
    private String longitude;

    // 위도
    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private Integer view;

    @Column(nullable = false)
    private Integer satisfaction;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String memberNickname;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Column(nullable = false)
    private Integer commentCount;

    public void update(BoardRequest.BoardUpdateRequest request){
        this.locationName = request.locationName();
        this.formattedAddress = request.formattedAddress();
        this.latitude = request.latitude();
        this.longitude = request.longitude();
        this.title = request.title();
        this.body = request.body();
        this.satisfaction = request.satisfaction();
    }

    public void addViewCount(Integer view) {
        this.view = view;
    }

    public void commentCountUp(){this.commentCount += 1;}

    public void commentCountDown(){this.commentCount -= 1;}

}

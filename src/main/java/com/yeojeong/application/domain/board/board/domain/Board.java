package com.yeojeong.application.domain.board.board.domain;

import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.member.member.domain.Member;
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

    @Column(nullable = false, length = 50000)
    private String body;

    @Column(nullable = false)
    private Integer view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Column(nullable = false)
    private Integer avgScore;

    @Column(nullable = false)
    private Integer commentCount;

    public void update(BoardRequest.PutBoard request){
        this.locationName = request.locationName();
        this.formattedAddress = request.formattedAddress();
        this.latitude = request.latitude();
        this.longitude = request.longitude();
        this.title = request.title();
        this.body = request.body();
    }

    public void addViewCount() {
        this.view++;
    }

    public void commentCountUp(){this.commentCount += 1;}

    public void commentCountDown(){this.commentCount -= 1;}

    public void avgScorePatch(Integer avgScore) {
        this.avgScore = avgScore;
    }

}

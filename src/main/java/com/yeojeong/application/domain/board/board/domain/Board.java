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

    public void update(Board updateEntity){
        this.locationName = updateEntity.getLocationName();
        this.formattedAddress = updateEntity.getFormattedAddress();
        this.latitude = updateEntity.getLatitude();
        this.longitude = updateEntity.getLongitude();
        this.title = updateEntity.getTitle();
        this.body = updateEntity.getBody();
    }

    public void addViewCount() {
        this.view++;
    }

    public void commentCountUp(){this.commentCount += 1;}

    public void commentCountDown(){this.commentCount -= 1;}

    public void avgScorePatch(int avgScore) {
        this.avgScore = avgScore;
    }

}

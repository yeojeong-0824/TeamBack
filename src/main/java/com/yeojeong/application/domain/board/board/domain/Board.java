package com.yeojeong.application.domain.board.board.domain;

import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTime implements Serializable {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<Comment> comments;

    @Column()
    private Long plannerId;

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

    public void updateCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void avgScorePatch(int avgScore) {
        this.avgScore = avgScore;
    }

}

package com.yeojeong.application.domain.board.board.domain;

import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    private Integer view = 0;

    @Builder.Default
    private Long plannerId = 0L;

    @Builder.Default
    private Integer avgScore = 0;

    @Builder.Default
    private Integer commentCount = 0;

    public void update(Board updateEntity){
        this.locationName = updateEntity.getLocationName();
        this.formattedAddress = updateEntity.getFormattedAddress();
        this.latitude = updateEntity.getLatitude();
        this.longitude = updateEntity.getLongitude();
        this.title = updateEntity.getTitle();
        this.body = updateEntity.getBody();
        this.plannerId = updateEntity.getPlannerId();
    }

    public void updateAvgScore(int avgScore) {
        this.avgScore = avgScore;
    }

    public void updateCommentCount(int commentCount) { this.commentCount = commentCount; }

    public void addViewCount() {
        this.view++;
    }
}

package com.yeojeong.application.domain.planner.planner.domain;

import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerRequest;
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
public class Planner extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int personnel;

    @Column
    private String subTitle;

    @Column(nullable = false)
    private int locationCount;

    @OneToMany(mappedBy = "planner", fetch = FetchType.LAZY)
    private List<Location> locations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    public void update(Planner updateEntity) {
        title = updateEntity.getTitle();
        personnel = updateEntity.getPersonnel();
        subTitle = updateEntity.getSubTitle();
    }

    public void addLocation(){
        locationCount += 1;
    }

    public void deleteLocation() {
        locationCount -= 1;
    }
}

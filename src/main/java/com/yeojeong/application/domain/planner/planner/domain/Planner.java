package com.yeojeong.application.domain.planner.planner.domain;

import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.domain.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Planner extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int personnel;

    @Column
    private String subTitle;

    @Builder.Default
    private int locationCount = 0;

    @OneToMany(mappedBy = "planner", fetch = FetchType.EAGER)
    private List<Location> locations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    public void update(Planner updateEntity) {
        title = updateEntity.getTitle();
        personnel = updateEntity.getPersonnel();
        subTitle = updateEntity.getSubTitle();
    }

    public void updateLocation(){
        locationCount = this.getLocations().size();
    }
}

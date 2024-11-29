package com.yeojeong.application.domain.planner.planner.domain;

import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.domain.Location;
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
    private int startYear;

    @Column(nullable = false)
    private int startMonth;

    @Column(nullable = false)
    private int startDay;

    @Column(nullable = false)
    private int startHour;

    @Column(nullable = false)
    private int startMinute;

    @Column(nullable = false)
    private int endYear;

    @Column(nullable = false)
    private int endMonth;

    @Column(nullable = false)
    private int endDay;

    @Column(nullable = false)
    private int endHour;

    @Column(nullable = false)
    private int endMinute;

    @Column(nullable = false)
    private int locationCount;

    @OneToMany(mappedBy = "planner", fetch = FetchType.LAZY)
    private List<Location> locations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    public void update(Planner entity) {
        title = entity.getTitle();

        startYear = entity.getStartYear();
        startMonth = entity.getStartMonth();
        startDay = entity.getStartDay();
        startHour = entity.getStartHour();
        startMinute = entity.getStartMinute();

        endYear = entity.getEndYear();
        endMonth = entity.getEndMonth();
        endDay = entity.getEndDay();
        endHour = entity.getEndHour();
        endMinute = entity.getEndMinute();
    }

    public void addLocation(){
        locationCount += 1;
    }

    public void deleteLocation() {
        locationCount -= 1;
    }
}

package com.yeojeong.application.domain.planner.planner.domain;

import com.yeojeong.application.config.util.BaseTime;
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
    private int endYear;

    @Column(nullable = false)
    private int endMonth;

    @Column(nullable = false)
    private int endDay;

    @OneToMany(mappedBy = "planner", fetch = FetchType.LAZY)
    private List<Location> locations;

    public void update(Planner entity) {
        title = entity.getTitle();

        startYear = entity.getStartYear();
        startMonth = entity.getStartMonth();
        startDay = entity.getStartDay();

        endYear = entity.getEndYear();
        endMonth = entity.getEndMonth();
        endDay = entity.getEndDay();
    }
}

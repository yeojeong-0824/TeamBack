package com.yeojeong.application.domain.planner.location.domain;

import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Location extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int travelTime;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int hour;

    @Column(nullable = false)
    private int minute;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String address;

    @Column
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planner_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Planner planner;

    public void update(Location entity) {
        date = entity.getDate();
        time = entity.getTime();
        place = entity.getPlace();
        address = entity.getAddress();
        memo = entity.getMemo();
    }
}

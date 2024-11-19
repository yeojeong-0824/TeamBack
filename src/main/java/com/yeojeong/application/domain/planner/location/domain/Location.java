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

    @Column(nullable = false)
    private int date;

    @Column
    private int time;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String address;

    @Column
    private String memo;

    @Column(nullable = false)
    private Long nextLocation;

}

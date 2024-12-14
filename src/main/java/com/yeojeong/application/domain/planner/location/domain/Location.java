package com.yeojeong.application.domain.planner.location.domain;

import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    private Long unixTime;

    @Column
    private int travelTime;

    @Column
    private String transportation;

    @Column
    private String transportationNote;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planner_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Planner planner;

    public void update(LocationRequest.Put dto) {
        this.unixTime = dto.unixTime();

        this.travelTime = dto.travelTime();
        this.transportation = dto.transportation();
        this.transportationNote = dto.transportationNote();

        this.place = dto.place();
        this.address = dto.address();
        this.phoneNumber = dto.phoneNumber();
        this.memo = dto.memo();
    }


}

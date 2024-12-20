package com.yeojeong.application.domain.planner.location.domain;

import com.yeojeong.application.config.util.BaseTime;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Location extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long unixTime;

    @Column
    private Integer travelTime;

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
    @JoinColumn(name = "planner_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Planner planner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    public void update(Location updateEntity) {
        this.unixTime = updateEntity.getUnixTime();

        this.travelTime = updateEntity.getTravelTime();
        this.transportation = updateEntity.getTransportation();
        this.transportationNote = updateEntity.getTransportationNote();

        this.place = updateEntity.getPlace();
        this.address = updateEntity.getAddress();
        this.phoneNumber = updateEntity.getPhoneNumber();
        this.memo = updateEntity.getMemo();
    }


}

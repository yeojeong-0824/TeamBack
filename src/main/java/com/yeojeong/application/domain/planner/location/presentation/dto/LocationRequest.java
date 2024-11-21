package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.Builder;

public class LocationRequest {
    @Builder
    public record Save(
            Integer travelTime,
            String date,
            String time,
            String place,
            String address,
            String memo
    ){
        public static Location toEntity (Save dto, Planner planner) {
            return Location.builder()
                    .date(dto.date())
                    .time(dto.time())
                    .place(dto.place())
                    .address(dto.address())
                    .memo(dto.memo())
                    .planner(planner)
                    .build();
        }
    }

    @Builder
    public record Put(
            String date,
            String time,
            String place,
            String address,
            String memo
    ){
        public static Location toEntity (Put dto) {
            return Location.builder()
                    .date(dto.date())
                    .time(dto.time())
                    .place(dto.place())
                    .address(dto.address())
                    .memo(dto.memo())
                    .build();
        }
    }

}

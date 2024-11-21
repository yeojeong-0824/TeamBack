package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.persistence.Column;
import lombok.Builder;

public class LocationRequest {
    @Builder
    public record Save(
            Integer travelTime,

            Integer year,
            Integer month,
            Integer day,

            Integer hour,
            Integer minute,

            String place,
            String address,
            String memo
    ){
        public static Location toEntity (Save dto, Planner planner) {
            return Location.builder()
                    .travelTime(dto.travelTime())

                    .year(dto.year())
                    .month(dto.month())
                    .day(dto.day())

                    .hour(dto.hour())
                    .minute(dto.minute())

                    .place(dto.place())
                    .address(dto.address())
                    .memo(dto.memo())
                    .build();
        }
    }

    @Builder
    public record Put(
            Integer travelTime,

            Integer year,
            Integer month,
            Integer day,

            Integer hour,
            Integer minute,

            String place,
            String address,
            String memo
    ){
        public static Location toEntity (Put dto) {
            return Location.builder()
                    .travelTime(dto.travelTime())

                    .year(dto.year())
                    .month(dto.month())
                    .day(dto.day())

                    .hour(dto.hour())
                    .minute(dto.minute())

                    .place(dto.place())
                    .address(dto.address())
                    .memo(dto.memo())
                    .build();
        }
    }

}

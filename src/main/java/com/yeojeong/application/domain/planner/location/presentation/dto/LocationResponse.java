package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import lombok.Builder;

public class LocationResponse {
    @Builder
    public record FindByPlannerId(
            Long id,
            String date,
            String time,
            String place,
            String address,
            String memo,
            String plannerTitle
    ) {
        public static FindByPlannerId toDto(Location entity) {
            return FindByPlannerId.builder()
                    .id(entity.getId())
                    .date(entity.getDate())
                    .time(entity.getTime())
                    .place(entity.getPlace())
                    .address(entity.getAddress())
                    .memo(entity.getMemo())
                    .plannerTitle(entity.getPlanner().getTitle())
                    .build();
        }
    }

    @Builder
    public record FindById(
            Long id,
            String date,
            String time,
            String place,
            String address,
            String memo
    ) {
        public static FindById toDto(Location entity) {
            return FindById.builder()
                    .id(entity.getId())
                    .date(entity.getDate())
                    .time(entity.getTime())
                    .place(entity.getPlace())
                    .address(entity.getAddress())
                    .memo(entity.getMemo())
                    .build();
        }
    }
}

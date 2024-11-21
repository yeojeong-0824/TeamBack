package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class LocationResponse {
    @Builder
    @Schema(name = "장소 조회")
    public record FindById(
            Long id,
            Integer travelTime,

            Integer year,
            Integer month,
            Integer day,

            Integer hour,
            Integer minute,

            String place,
            String address,
            String memo
    ) {
        public static FindById toDto(Location entity) {
            return FindById.builder()
                    .id(entity.getId())

                    .travelTime(entity.getTravelTime())

                    .year(entity.getYear())
                    .month(entity.getMonth())
                    .day(entity.getDay())

                    .hour(entity.getHour())
                    .minute(entity.getMinute())

                    .place(entity.getPlace())
                    .address(entity.getAddress())
                    .memo(entity.getMemo())
                    .build();
        }
    }
}

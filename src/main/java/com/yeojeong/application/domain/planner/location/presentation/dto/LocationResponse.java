package com.yeojeong.application.domain.planner.location.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

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
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getUnixTime()), TimeZone.getDefault().toZoneId());
            return FindById.builder()
                    .id(entity.getId())

                    .travelTime(entity.getTravelTime())

                    .year(localDateTime.getYear())
                    .month(localDateTime.getMonthValue())
                    .day(localDateTime.getDayOfMonth())

                    .hour(localDateTime.getHour())
                    .minute(localDateTime.getMinute())

                    .place(entity.getPlace())
                    .address(entity.getAddress())
                    .memo(entity.getMemo())
                    .build();
        }
    }
}

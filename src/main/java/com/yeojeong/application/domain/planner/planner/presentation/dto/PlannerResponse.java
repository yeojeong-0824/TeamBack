package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.Builder;

import java.util.List;

public class PlannerResponse {

    @Builder
    public record FindById(
            Long id,
            String title,

            Integer startYear,
            Integer startMonth,
            Integer startDay,
            Integer startHour,
            Integer startMinute,

            Integer endYear,
            Integer endMonth,
            Integer endDay,
            Integer endHour,
            Integer endMinute,

            int locationCount,
            List<LocationResponse.FindById> locationInfo
    ) {
        public static FindById toDto(Planner planner, List<LocationResponse.FindById> locationInfo) {
            return FindById.builder()
                    .id(planner.getId())
                    .title(planner.getTitle())

                    .startYear(planner.getStartYear())
                    .startMonth(planner.getStartMonth())
                    .startDay(planner.getStartDay())
                    .startHour(planner.getStartHour())
                    .startMinute(planner.getStartMinute())

                    .endYear(planner.getEndYear())
                    .endMonth(planner.getEndMonth())
                    .endDay(planner.getEndDay())
                    .endHour(planner.getEndHour())
                    .endMinute(planner.getEndMinute())

                    .locationCount(planner.getLocationCount())
                    .locationInfo(locationInfo)
                    .build();
        }
    }
}

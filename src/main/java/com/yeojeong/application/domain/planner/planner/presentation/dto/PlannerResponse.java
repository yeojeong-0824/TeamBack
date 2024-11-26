package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.Builder;

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
            Integer endMinute
    ) {
        public static FindById toDto(Planner planner) {
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
                    .build();
        }
    }
}

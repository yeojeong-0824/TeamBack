package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.Builder;

import java.util.List;

public class PlannerRequest {
    @Builder
    public record Save(
            String title,

            Integer startYear,
            Integer startMonth,
            Integer startDay,

            Integer endYear,
            Integer endMonth,
            Integer endDay

    ) {
        public static Planner toEntity(PlannerRequest.Save dto) {
            return Planner.builder()
                    .title(dto.title())

                    .startYear(dto.startYear())
                    .startMonth(dto.startMonth())
                    .startDay(dto.startDay())

                    .endYear(dto.endYear())
                    .endMonth(dto.endMonth())
                    .endDay(dto.endDay())
                    .build();
        }
    }

    public record Put(
        String title,

        Integer startYear,
        Integer startMonth,
        Integer startDay,

        Integer endYear,
        Integer endMonth,
        Integer endDay
    ) {
        public static Planner toEntity(PlannerRequest.Put dto) {
            return Planner.builder()
                    .title(dto.title())

                    .startYear(dto.startYear())
                    .startMonth(dto.startMonth())
                    .startDay(dto.startDay())

                    .endYear(dto.endYear())
                    .endMonth(dto.endMonth())
                    .endDay(dto.endDay())
                    .build();
        }
    }
}

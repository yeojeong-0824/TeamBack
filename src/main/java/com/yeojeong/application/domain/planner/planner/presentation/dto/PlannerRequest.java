package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

public class PlannerRequest {
    @Builder
    public record Save(
            @NotNull @Size(min = 1, max = 30)
            String title,

            @Min(2024)
            Integer startYear,
            @Min(1) @Max(12)
            Integer startMonth,
            @Min(1) @Max(31)
            Integer startDay,

            Integer endYear,
            @Min(1) @Max(12)
            Integer endMonth,
            @Min(1) @Max(31)
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
            @NotNull @Size(min = 1, max = 30)
            String title,

            @Min(2024)
            Integer startYear,
            @Min(1) @Max(12)
            Integer startMonth,
            @Min(1) @Max(31)
            Integer startDay,

            @Min(2024)
            Integer endYear,
            @Min(1) @Max(12)
            Integer endMonth,
            @Min(1) @Max(31)
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

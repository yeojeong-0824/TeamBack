package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.Builder;

import java.util.List;

public class PlannerRequest {
    @Builder
    public record Save(
            String title,
            String startDate,
            String endDate

    ) {
        public static Planner toEntity(PlannerRequest.Save dto) {
            return Planner.builder()
                    .title(dto.title())
                    .startDate(dto.startDate())
                    .endDate(dto.endDate())
                    .build();
        }
    }

    public record Put(
        String title,
        String startDate,
        String endDate
    ) {
        public static Planner toEntity(PlannerRequest.Put dto) {
            return Planner.builder()
                    .title(dto.title())
                    .startDate(dto.startDate())
                    .endDate(dto.endDate())
                    .build();
        }
    }
}

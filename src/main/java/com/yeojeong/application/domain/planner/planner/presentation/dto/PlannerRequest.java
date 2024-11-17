package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.Builder;

public class PlannerRequest {
    @Builder
    public record Save(
            String title

    ) {
        public static Planner toEntity(PlannerRequest.Save dto) {
            return Planner.builder()
                    .title(dto.title())
                    .build();
        }
    }

    public record Put(
        String title
    ) {
        public static Planner toEntity(PlannerRequest.Put dto) {
            return Planner.builder()
                    .title(dto.title())
                    .build();
        }
    }
}

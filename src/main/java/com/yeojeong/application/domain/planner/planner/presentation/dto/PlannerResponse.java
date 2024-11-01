package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.Builder;

public class PlannerResponse {

    @Builder
    public record FindById(
            Long id,
            String title
    ) {
        public static PlannerResponse.FindById toDto(Planner planner) {
            return PlannerResponse.FindById.builder()
                    .id(planner.getId())
                    .title(planner.getTitle())
                    .build();
        }
    }
}

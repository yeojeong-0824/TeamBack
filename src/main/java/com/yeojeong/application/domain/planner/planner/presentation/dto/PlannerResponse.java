package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.Builder;

public class PlannerResponse {

    @Builder
    public record FindById(
            Long id,
            String title,
            String startDate,
            String endDate
    ) {
        public static FindById toDto(Planner planner) {
            return FindById.builder()
                    .id(planner.getId())
                    .title(planner.getTitle())
                    .startDate(planner.getStartDate())
                    .endDate(planner.getEndDate())
                    .build();
        }
    }
}

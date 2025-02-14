package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.validation.constraints.*;

public class PlannerRequest {
    public record PlannerSave(
            @NotNull @Size(min = 1, max = 30)
            String title,
            @NotNull
            int personnel,
            @Size(max = 30)
            String subTitle

    ) {
        public static Planner toEntity(PlannerSave dto, Member member) {
            return Planner.builder()
                    .title(dto.title())
                    .personnel(dto.personnel())
                    .subTitle(dto.subTitle())
                    .member(member)
                    .build();
        }
    }

    public record PlannerPut(
            @NotNull @Size(min = 1, max = 30)
            String title,
            @NotNull
            int personnel,
            @Size(max = 30)
            String subTitle
    ) {
        public static Planner toEntity(PlannerPut dto) {
            return Planner.builder()
                    .title(dto.title())
                    .personnel(dto.personnel())
                    .subTitle(dto.subTitle())
                    .build();
        }
    }
}

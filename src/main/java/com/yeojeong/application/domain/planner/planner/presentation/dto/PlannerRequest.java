package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import jakarta.validation.constraints.*;
import lombok.Builder;

public class PlannerRequest {
    public record Save(
            @NotNull @Size(min = 1, max = 30)
            String title,
            @NotNull
            int personnel,
            @Size(max = 30)
            String subTitle

    ) {
        public static Planner toEntity(PlannerRequest.Save dto, Member member) {
            return Planner.builder()
                    .title(dto.title())
                    .personnel(dto.personnel())
                    .subTitle(dto.subTitle())
                    .member(member)
                    .build();
        }
    }

    public record Put(
            @NotNull @Size(min = 1, max = 30)
            String title,
            @NotNull
            int personnel,
            @Size(max = 30)
            String subTitle
    ) {
        public static Planner toEntity(PlannerRequest.Put dto) {
            return Planner.builder()
                    .title(dto.title())
                    .personnel(dto.personnel())
                    .subTitle(dto.subTitle())
                    .build();
        }
    }
}

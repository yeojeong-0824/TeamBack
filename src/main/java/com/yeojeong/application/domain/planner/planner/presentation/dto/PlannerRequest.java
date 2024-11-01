package com.yeojeong.application.domain.planner.planner.presentation.dto;

import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.Builder;

public class PlannerRequest {
    @Builder
    public record Save(
            String title

    ) {
        public static Planner toEntity(BoardRequest.Save dto, Member member) {
            return Planner.builder()
                    .title(dto.title())
                    .member(member)
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

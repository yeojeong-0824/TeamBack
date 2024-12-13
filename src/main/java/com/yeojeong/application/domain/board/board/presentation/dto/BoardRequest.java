package com.yeojeong.application.domain.board.board.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class BoardRequest {

    @Builder
    public record Save(
            @NotBlank// 도
            String title,

            @NotBlank
            String body,

            @NotBlank
            String locationName,

            @NotBlank
            String formattedAddress,

            @NotBlank
            String latitude,

            @NotBlank
            String longitude,

            Long plannerId
    ) {
        public static Board toEntity(Save dto, Member member) {
            return Board.builder()
                    .locationName(dto.locationName())
                    .formattedAddress(dto.formattedAddress())
                    .latitude(dto.latitude())
                    .longitude(dto.longitude())
                    .title(dto.title())
                    .body(dto.body())
                    .view(0)
                    .commentCount(0)
                    .avgScore(0)

                    .member(member)
                    .plannerId(dto.plannerId())
                    .build();
        }
    }

    public record Put(
            @NotBlank
            String title,

            @NotBlank
            String body,

            @NotBlank
            String locationName,

            @NotBlank
            String formattedAddress,

            @NotBlank
            String latitude,

            @NotBlank
            String longitude,

            Long plannerId
    ) {
        public static Board toEntity(Put dto) {
            return Board.builder()
                    .locationName(dto.locationName())
                    .formattedAddress(dto.formattedAddress())
                    .latitude(dto.latitude())
                    .longitude(dto.longitude())
                    .title(dto.title())
                    .body(dto.body())

                    .plannerId(dto.plannerId())
                    .build();
        }
    }

}

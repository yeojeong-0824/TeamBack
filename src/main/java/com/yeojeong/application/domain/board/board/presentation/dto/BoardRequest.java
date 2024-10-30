package com.yeojeong.application.domain.board.board.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.member.domain.Member;
import lombok.Builder;

public class BoardRequest {

    @Builder
    public record Save(
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            String body
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
                    .member(member)
                    .avgScore(0)
                    .build();
        }
    }

    public record Put(
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            String body
    ) {
        public static Board toEntity(Put dto) {
            return Board.builder()
                    .locationName(dto.locationName())
                    .formattedAddress(dto.formattedAddress())
                    .latitude(dto.latitude())
                    .longitude(dto.longitude())
                    .title(dto.title())
                    .body(dto.body())
                    .build();
        }
    }

}

package com.example.demo.domain.board.board.presentation.dto;

import com.example.demo.domain.board.board.domain.Board;
import com.example.demo.domain.member.member.domain.Member;
import lombok.Builder;

public class BoardRequest {

    @Builder
    public record SaveBoard(
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            String body
    ) {
        public static Board toEntity(SaveBoard dto, Member member) {
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

    public record PutBoard(
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            String body
    ) {
    }

}

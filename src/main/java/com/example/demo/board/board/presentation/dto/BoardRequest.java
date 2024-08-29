package com.example.demo.board.board.presentation.dto;

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

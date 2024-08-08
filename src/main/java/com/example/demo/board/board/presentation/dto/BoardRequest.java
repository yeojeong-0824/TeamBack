package com.example.demo.board.board.presentation.dto;

public class BoardRequest {

    public record DefaultBoard(
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            String body,
            Integer satisfaction
    ) {
    }

    public record BoardUpdateRequest(
            String locationName,
            String formattedAddress,
            String latitude,  // 위도
            String longitude,  // 경도
            String title,
            String body,
            Integer satisfaction
    ) {
    }

}

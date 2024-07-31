package com.example.demo.dto.board;

public class BoardRequest {

    public record BoardSaveRequest(
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

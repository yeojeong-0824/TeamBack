package com.example.demo.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;

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

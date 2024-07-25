package com.example.demo.dto.board;

public class BoardRequest {

    public record BoardSaveRequest(
            String country,
            String city,
            String title,
            String body,
            Integer satisfaction
    ) {
    }

    public record BoardUpdateRequest(
            String country,
            String city,
            String title,
            String body,
            Integer satisfaction
    ) {
    }

}

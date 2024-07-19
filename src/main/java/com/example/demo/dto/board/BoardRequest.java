package com.example.demo.dto.board;

public class BoardRequest {

    public record BoardSaveRequest(
            String title,
            String body,
            String memberName,
            Integer satisfaction
    ) {
    }

    public record BoardUpdateRequest(
            String title,
            String body,
            Integer satisfaction
    ) {
    }

}

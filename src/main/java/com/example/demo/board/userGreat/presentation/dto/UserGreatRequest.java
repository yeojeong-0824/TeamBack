package com.example.demo.board.userGreat.presentation.dto;

public class UserGreatRequest {
    public record SaveUserGreat(
            Long userId,
            Long boardId,
            Short score
    ) {}
}

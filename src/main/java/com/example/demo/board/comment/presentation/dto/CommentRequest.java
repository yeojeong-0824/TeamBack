package com.example.demo.board.comment.presentation.dto;

public class CommentRequest {

    public record CommentSaveRequest(
            String body
    ){
    }

    public record CommentUpdateRequest(
            String body
    ) {
    }
}

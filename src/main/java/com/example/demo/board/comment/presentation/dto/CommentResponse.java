package com.example.demo.board.comment.presentation.dto;

import com.example.demo.board.comment.domain.Comment;

public class CommentResponse {

    public record CommentSaveResponse(
            String body,
            String memberNickname
    ) {
        public CommentSaveResponse(Comment comment){
            this (
                    comment.getBody(),
                    comment.getMember().getNickname()
            );
        }
    }

    public record CommentListResponse (
            Long id,
            String body,
            String memberNickname
    ) {
        public CommentListResponse(Comment comment){
            this (
                    comment.getId(),
                    comment.getBody(),
                    comment.getMember().getNickname()
            );
        }
    }
}

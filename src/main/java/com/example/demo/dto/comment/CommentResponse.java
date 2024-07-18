package com.example.demo.dto.comment;

import com.example.demo.entity.Comment;

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

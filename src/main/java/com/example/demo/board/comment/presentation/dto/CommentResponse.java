package com.example.demo.board.comment.presentation.dto;

import com.example.demo.board.comment.domain.Comment;
import lombok.Builder;

public class CommentResponse {
    @Builder
    public record FindByBoardId(
            Integer score,
            String comment
    ) {
        @Builder
        private record MemberInfo(
                Long id,
                String nickname
        ){}

        public static FindByBoardId toDto(Comment entity) {
            return FindByBoardId.builder()
                    .score(entity.getScore())
                    .
        }
    }
}

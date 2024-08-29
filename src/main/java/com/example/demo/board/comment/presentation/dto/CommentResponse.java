package com.example.demo.board.comment.presentation.dto;

import com.example.demo.board.comment.domain.Comment;
import lombok.Builder;

public class CommentResponse {
    @Builder
    public record FindByBoardId(
            Long id,
            Integer score,
            String comment,
            MemberInfo member
    ) {
        @Builder
        private record MemberInfo(
                Long id,
                String nickname
        ){}

        public static FindByBoardId toDto(Comment entity) {

            return FindByBoardId.builder()
                    .id(entity.getId())
                    .score(entity.getScore())
                    .comment(entity.getComment())
                    .member(MemberInfo.builder()
                            .id(entity.getMember().getId())
                            .nickname(entity.getMember().getNickname())
                            .build())
                    .build();
        }
    }

}

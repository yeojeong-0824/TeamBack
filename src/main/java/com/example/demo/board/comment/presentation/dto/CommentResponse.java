package com.example.demo.board.comment.presentation.dto;

import com.example.demo.board.comment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class CommentResponse {
    @Builder
    @Schema(name = "게시글에 작성된 댓글 조회")
    public record FindByBoardId(
            @Schema(example = "1")
            Long id,

            @Schema(example = "3")
            Integer score,

            @Schema(example = "여기 맛집임")
            String comment,

            MemberInfo member
    ) {
        @Builder
        private record MemberInfo(
                @Schema(example = "1")
                Long id,

                @Schema(example = "걸리버")
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

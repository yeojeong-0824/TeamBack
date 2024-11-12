package com.yeojeong.application.domain.board.comment.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

public class CommentResponse {
    @Builder
    @Schema(name = "게시글에 작성된 댓글 조회")
    public record FindByBoardId(
            Long id,
            Integer score,
            String comment,

            MemberInfo member,
            TimeInfo time
    ) {
        @Builder
        private record MemberInfo(
                Long id,
                String nickname
        ){}

        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
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

    @Builder
    public record FindById(
            Long id,
            Integer score,
            String comment,

            MemberInfo member,
            BoardInfo board,
            FindByBoardId.TimeInfo time
    ) {
        @Builder
        public record MemberInfo(
                Long id,
                String nickname
        ){}

        @Builder
        public record BoardInfo(
                Long id,
                String title,
                Integer score
        ){}

        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
        ){}

        public static FindById toDto(Comment entity) {
            return FindById.builder()
                    .id(entity.getId())
                    .score(entity.getScore())
                    .comment(entity.getComment())
                    .member(MemberInfo.builder()
                            .id(entity.getMember().getId())
                            .nickname(entity.getMember().getNickname())
                            .build())
                    .board(BoardInfo.builder()
                            .id(entity.getBoard().getId())
                            .title(entity.getBoard().getTitle())
                            .score(entity.getBoard().getAvgScore())
                            .build())
                    .build();
        }
    }

    @Builder
    public record Delete(
            BoardInfo board
    ) {
        @Builder
        public record BoardInfo(
                Long id,
                String title,
                Integer score
        ){}

        public static Delete toDto(Board board) {
            return Delete.builder()
                    .board(BoardInfo.builder()
                            .id(board.getId())
                            .title(board.getTitle())
                            .score(board.getAvgScore())
                            .build())
                    .build();
        }
    }

}

package com.yeojeong.application.domain.member.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class MemberResponse {
    @Builder
    @Schema(name = "유저 정보 호출")
    public record FindById(
        String username,
        String nickname,
        String email,
        Integer age
    ) {
        public static FindById toDto(Member entity) {
            return FindById.builder()
                    .username(entity.getUsername())
                    .nickname(entity.getNickname())
                    .email(entity.getEmail())
                    .age(entity.getAge())
                    .build();
        }
    }

    @Builder
    @Schema(name = "유저가 작성한 게시글 정보 호출")
    public record BoardInfo(
            Long id,
            String title
    ){
        public static BoardInfo toDto(Board board) {
            return BoardInfo.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .build();
        }
    }

    @Builder
    @Schema(name = "유저가 작성한 댓글 정보 호출")
    public record CommentInfo(
            CommentBoardInfo boardInfo,
            Integer score,
            String comment
    ){
        @Builder
        private record CommentBoardInfo(
                Long id,
                String title
        ){}
        public static CommentInfo toDto(Comment comment) {
            return CommentInfo.builder()
                    .boardInfo(CommentBoardInfo.builder()
                            .id(comment.getBoard().getId())
                            .title(comment.getBoard().getTitle())
                            .build())
                    .score(comment.getScore())
                    .comment(comment.getComment())
                    .build();
        }
    }
}

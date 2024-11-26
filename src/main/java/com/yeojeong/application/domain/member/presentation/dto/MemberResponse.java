package com.yeojeong.application.domain.member.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class MemberResponse {
    @Builder
    @Schema(name = "유저 정보 호출")
    public record FindById(
        String username,
        String nickname,
        String email,
        Integer age,
        TimeInfo time
    ) {
        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
        ) {}
        public static FindById toDto(Member entity) {
            return FindById.builder()
                    .username(entity.getUsername())
                    .nickname(entity.getNickname())
                    .email(entity.getEmail())
                    .age(entity.getAge())
                    .time(TimeInfo.builder()
                            .createTime(entity.getCreateAt())
                            .updateTime(entity.getUpdateAt())
                            .build())
                    .build();
        }
    }

    @Builder
    @Schema(name = "유저가 작성한 게시글 정보 호출")
    public record BoardInfo(
            Long id,
            String title,
            TimeInfo time
    ){
        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
        ) {}

        public static BoardInfo toDto(Board board) {
            return BoardInfo.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .time(TimeInfo.builder()
                            .createTime(board.getCreateAt())
                            .updateTime(board.getUpdateAt())
                            .build())
                    .build();
        }
    }

    @Builder
    @Schema(name = "유저가 작성한 댓글 정보 호출")
    public record CommentInfo(
            CommentBoardInfo boardInfo,
            Integer score,
            String comment,
            TimeInfo time
    ){
        @Builder
        private record TimeInfo(
                LocalDateTime createTime,
                LocalDateTime updateTime
        ) {}
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
                    .time(TimeInfo.builder()
                            .createTime(comment.getCreateAt())
                            .updateTime(comment.getUpdateAt())
                            .build())
                    .build();
        }
    }

    @Builder
    public record patchKey(
            String key
    ) {}
}

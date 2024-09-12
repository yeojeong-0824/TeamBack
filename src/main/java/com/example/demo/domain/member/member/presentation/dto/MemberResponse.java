package com.example.demo.domain.member.member.presentation.dto;

import com.example.demo.domain.board.board.domain.Board;
import com.example.demo.domain.board.comment.domain.Comment;
import com.example.demo.domain.member.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class MemberResponse {
    @Builder
    @Schema(name = "유저 정보 호출")
    public record FindMember (
        @Schema(example = "user12")
        String username,

        @Schema(example = "소인국갔다옴")
        String nickname,

        @Schema(example = "example@naver.com")
        String email,

        @Schema(example = "90")
        Integer age
    ) {
        public static FindMember toDto(Member entity) {
            return FindMember.builder()
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
            @Schema(example = "1")
            Long id,

            @Schema(example = "소인국 갔다온 썰 푼다")
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

            @Schema(example = "5")
            Integer score,

            @Schema(example = "소인국 가보니까 진짜 쪼만했음")
            String comment
    ){
        @Builder
        private record CommentBoardInfo(
                @Schema(example = "1")
                Long id,

                @Schema(example = "소인국 갔다온 썰 푼다")
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

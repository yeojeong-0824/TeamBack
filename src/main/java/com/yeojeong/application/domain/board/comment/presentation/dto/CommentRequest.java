package com.yeojeong.application.domain.board.comment.presentation.dto;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class CommentRequest {
    public record Save(
            @NotNull
            @Min(0) @Max(5)
            Integer score,

            @NotBlank
            String comment
    ){
        public static Comment toEntity(Save dto, Board board, Member member) {
            return Comment.builder()
                    .score(dto.score())
                    .comment(dto.comment())
                    .board(board)
                    .member(member)
                    .build();
        }
    }

    public record Put(
            @NotNull
            @Min(0) @Max(5)
            Integer score,

            @NotBlank
            String comment
    ){
        public static Comment toEntity(Put dto) {
            return Comment.builder()
                    .score(dto.score())
                    .comment(dto.comment())
                    .build();
        }
    }
}

package com.example.demo.domain.board.comment.presentation.dto;


import com.example.demo.domain.board.board.domain.Board;
import com.example.demo.domain.board.comment.domain.Comment;
import com.example.demo.domain.member.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class CommentRequest {
    @Builder
    @Schema(name = "댓글 작성")
    public record Save(
            @NotNull
            @Min(0) @Max(5)
            @Schema(example = "3", description = "0이면 평점을 넣지 않은 것으로 간주합니다")
            Integer score,

            @NotBlank
            @Schema(example = "여기 맛집임")
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

    @Schema(name = "댓글 수정")
    public record Edit(
            @NotNull
            @Min(0) @Max(5)
            @Schema(example = "3", description = "0이면 평점을 넣지 않은 것으로 간주합니다")
            Integer score,

            @NotBlank
            @Schema(example = "여기 맛집임")
            String comment
    ){
    }
}

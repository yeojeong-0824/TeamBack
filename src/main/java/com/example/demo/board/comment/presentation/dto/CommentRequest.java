package com.example.demo.board.comment.presentation.dto;


import com.example.demo.board.board.domain.Board;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.member.member.domain.Member;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class CommentRequest {
    @Builder
    public record Save(
            @NotBlank
            @Min(1) @Max(5)
            Integer score,
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
}

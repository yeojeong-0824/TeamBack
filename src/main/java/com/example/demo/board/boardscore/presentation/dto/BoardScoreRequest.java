package com.example.demo.board.boardscore.presentation.dto;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.boardscore.domain.BoardScore;
import com.example.demo.member.member.domain.Member;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

public class BoardScoreRequest {
    @Builder
    public record SaveScore(
            @Min(1) @Max(10)
            int score
    ) {
        public static BoardScore toEntity(SaveScore dto, Board board, Member member) {
            return BoardScore.builder()
                    .board(board)
                    .member(member)
                    .score(dto.score())
                    .build();
        }
    }
}

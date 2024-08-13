package com.example.demo.board.boardscore.presentation.dto;

import com.example.demo.board.boardscore.domain.BoardScore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

public class BoardScoreRequest {
    @Builder
    public record SaveScore(
            @Min(1) @Max(10)
            int score
    ) {
        public static BoardScore toEntity(SaveScore dto, Long boardId, Long userId) {
            return BoardScore.builder()
                    .boardId(boardId)
                    .userId(userId)
                    .score(dto.score())
                    .build();
        }
    }
}

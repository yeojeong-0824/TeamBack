package com.example.demo.board.boardscore.presentation.dto;

import com.example.demo.board.boardscore.domain.BoardScore;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public class BoardScoreResponse {
    @Builder
    public record BoardScoreByBoardId(
            int size,
            List<Long> userIdList,
            int avgScore
    ){
        public static BoardScoreByBoardId toDto(List<BoardScore> entityList) {
            if(entityList.isEmpty()) return BoardScoreByBoardId.builder()
                    .size(0)
                    .userIdList(null)
                    .avgScore(0)
                    .build();

            List<Long> userIdList = new ArrayList<>();
            int sumScore = 0;
            int avgScore;
            for(BoardScore entity : entityList) {
                sumScore += entity.getScore();
                userIdList.add(entity.getUserId());
            }
            avgScore = sumScore / userIdList.size();

            return BoardScoreByBoardId.builder()
                    .size(userIdList.size())
                    .userIdList(userIdList)
                    .avgScore(avgScore)
                    .build();
        }
    }
}

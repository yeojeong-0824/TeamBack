package com.example.demo.board.boardscore.application;

import com.example.demo.board.boardscore.presentation.dto.BoardScoreRequest;

public interface BoardScoreService {
    void save(BoardScoreRequest.SaveScore takenDto, Long takenBoardId, Long takenUserId);
}

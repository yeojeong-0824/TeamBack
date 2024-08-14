package com.example.demo.board.boardscore.application;

import com.example.demo.board.boardscore.presentation.dto.BoardScoreRequest;
import com.example.demo.board.boardscore.presentation.dto.BoardScoreResponse;

public interface BoardScoreService {
    void save(BoardScoreRequest.SaveScore takenDto, Long takenBoardId, Long takenUserId);
}

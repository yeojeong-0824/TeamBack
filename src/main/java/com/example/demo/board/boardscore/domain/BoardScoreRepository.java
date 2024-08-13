package com.example.demo.board.boardscore.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardScoreRepository extends JpaRepository<BoardScore, Long> {
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
    List<BoardScore> findAllByBoardId(Long boardId);

    void deleteByBoardId(Long boardId);
}

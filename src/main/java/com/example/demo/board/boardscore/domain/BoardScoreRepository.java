package com.example.demo.board.boardscore.domain;

import com.example.demo.board.board.domain.Board;
import com.example.demo.member.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardScoreRepository extends JpaRepository<BoardScore, Long> {
    void deleteByMember(Member member);
    void deleteByBoard_IdAndMember_id(Long boardId, Long memberId);
    List<BoardScore> findByBoard(Board board);
}
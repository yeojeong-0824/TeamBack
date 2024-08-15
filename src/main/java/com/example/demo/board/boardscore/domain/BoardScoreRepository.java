package com.example.demo.board.boardscore.domain;

import com.example.demo.member.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardScoreRepository extends JpaRepository<BoardScore, Long> {
    void deleteByMember(Member member);
}
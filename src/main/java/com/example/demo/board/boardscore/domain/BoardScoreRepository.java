package com.example.demo.board.boardscore.domain;

import com.example.demo.board.board.domain.Board;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.presentation.dto.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardScoreRepository extends JpaRepository<BoardScore, Long> {
    void deleteByMember(Member member);
    void deleteByBoard_IdAndMember_Id(Long boardId, Long memberId);
    Optional<BoardScore> findByBoard_IdAndMember_Id(Long boardId, Long memberId);
    boolean existsByBoardAndMember(Board board, Member member);
    List<BoardScore> findByBoard(Board board);
    Page<BoardScore> findByMember(Member member, Pageable pageable);
}
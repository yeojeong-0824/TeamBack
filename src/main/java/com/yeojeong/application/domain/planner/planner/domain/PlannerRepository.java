package com.yeojeong.application.domain.planner.planner.domain;

import com.yeojeong.application.domain.board.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
    Page<Planner> findAllByMemberId(Long memberId, Pageable pageable);
}

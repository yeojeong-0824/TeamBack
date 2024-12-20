package com.yeojeong.application.domain.planner.planner.application.plannerservice;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import org.springframework.data.domain.Page;

public interface PlannerService {
    Planner save(Planner entity);
    Planner update(Planner entity);
    Planner findById(Long id);
    Page<Planner> findByMemberId(Long memberId, int page);

    void delete(Planner entity);
    void deleteByMemberId(Long memberId);
}

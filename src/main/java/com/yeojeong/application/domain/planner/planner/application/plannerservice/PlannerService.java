package com.yeojeong.application.domain.planner.planner.application.plannerservice;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import org.springframework.data.domain.Page;

public interface PlannerService {
    Planner save(Planner entity);
    Planner update(Planner entity);
    Planner findById(Long id);
    void delete(Planner entity);
    Page<Planner> findByMember(Long memberId, int page);
}

package com.yeojeong.application.domain.planner.planner.application.plannerservice;

import com.yeojeong.application.domain.planner.planner.domain.Planner;
import org.springframework.data.domain.Page;

public interface PlannerService {
    Planner save(Planner entity);
    Planner update(Planner entity, Planner updateEntity);
    Planner findById(Long id);
    Page<Planner> findByMemberId(Long memberId, int page);
    void updateLocation(Planner entity);

    void delete(Planner entity);
    void deleteByMemberId(Long memberId);
}

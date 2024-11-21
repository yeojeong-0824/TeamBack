package com.yeojeong.application.domain.planner.planner.application.plannerservice;

import com.yeojeong.application.domain.planner.planner.domain.Planner;

public interface PlannerService {
    Planner save(Planner entity);
    Planner update(Planner entity);
    Planner findById(Long id);
    void delete(Planner entity);
}

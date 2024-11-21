package com.yeojeong.application.domain.planner.planner.application.plannerfacade;

import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerRequest;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;

public interface PlannerFacade {
    PlannerResponse.FindById save(PlannerRequest.Save dto);
    PlannerResponse.FindById update(Long id, PlannerRequest.Put dto);
    void delete(Long id);
    PlannerResponse.FindById findById(Long id);

}

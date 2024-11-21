package com.yeojeong.application.domain.planner.planner.application.plannerfacade.implement;

import com.yeojeong.application.domain.planner.planner.application.plannerfacade.PlannerFacade;
import com.yeojeong.application.domain.planner.planner.application.plannerservice.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerFacadeImpl implements PlannerFacade {
    private final PlannerService plannerService;

    @Override
    public PlannerResponse.FindById save(PlannerRequest.Save dto) {
        Planner entity = PlannerRequest.Save.toEntity(dto);
        Planner savedEntity = plannerService.save(entity);

        return PlannerResponse.FindById.toDto(savedEntity);
    }

    @Override
    public PlannerResponse.FindById update(Long id, PlannerRequest.Put dto) {
        Planner savedEntity = plannerService.findById(id);
        Planner entity = PlannerRequest.Put.toEntity(dto);
        savedEntity.update(entity);

        Planner rtnEntity = plannerService.update(savedEntity);
        return PlannerResponse.FindById.toDto(rtnEntity);
    }

    @Override
    public void delete(Long id) {
        Planner savedEntity = plannerService.findById(id);

        plannerService.delete(savedEntity);
    }

    @Override
    public PlannerResponse.FindById findById(Long id) {
        Planner savedEntity = plannerService.findById(id);
        return PlannerResponse.FindById.toDto(savedEntity);
    }
}

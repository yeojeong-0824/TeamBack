package com.yeojeong.application.domain.planner.planner.application.plannerservice.implement;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.planner.planner.application.plannerservice.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.planner.planner.domain.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerServiceImpl implements PlannerService {

    private final PlannerRepository plannerRepository;

    @Override
    public Planner save(Planner entity) {
        return plannerRepository.save(entity);
    }

    @Override
    public Planner update(Planner entity) {
        return plannerRepository.save(entity);
    }

    @Override
    public Planner findById(Long id) {
        return plannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 플래너를 찾을 수 없습니다."));
    }

    @Override
    public void delete(Planner entity) {
        plannerRepository.delete(entity);
    }
}
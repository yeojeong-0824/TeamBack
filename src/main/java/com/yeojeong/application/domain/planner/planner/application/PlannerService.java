package com.yeojeong.application.domain.planner.planner.application;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.planner.planner.domain.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;

    public Planner save(Planner entity) {
        return plannerRepository.save(entity);
    }

    public Planner update(Planner entity, Planner updateEntity) {
        entity.update(updateEntity);
        return plannerRepository.save(entity);
    }

    public Planner findById(Long id) {
        return plannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 플래너를 찾을 수 없습니다."));
    }

    public Page<Planner> findByMemberId(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        return plannerRepository.findAllByMemberId(memberId, pageRequest);
    }

    public void updateLocation(Planner entity) {
        entity.updateLocation();
        plannerRepository.save(entity);
    }

    public void delete(Planner entity) {
        plannerRepository.delete(entity);
    }

    public void deleteByMemberId(Long memberId) {
        plannerRepository.deleteByMemberId(memberId);
    }
}

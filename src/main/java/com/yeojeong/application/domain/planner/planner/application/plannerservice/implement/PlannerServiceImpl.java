package com.yeojeong.application.domain.planner.planner.application.plannerservice.implement;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.application.plannerservice.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.planner.planner.domain.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannerServiceImpl implements PlannerService {

    private final PlannerRepository plannerRepository;

    @Override
    public Planner save(Planner entity) {
        return plannerRepository.save(entity);
    }

    @Override
    public Planner update(Planner entity, Planner updateEntity) {
        entity.update(updateEntity);
        return plannerRepository.save(entity);
    }

    @Override
    public Planner findById(Long id) {
        return plannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 플래너를 찾을 수 없습니다."));
    }

    @Override
    public Page<Planner> findByMemberId(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        return plannerRepository.findAllByMemberId(memberId, pageRequest);
    }

    @Override
    public void addLocation(Planner entity) {
        entity.addLocation();
        plannerRepository.save(entity);
    }

    @Override
    public void deleteLocation(Planner entity) {
        entity.deleteLocation();
        plannerRepository.save(entity);
    }

    @Override
    public void delete(Planner entity) {
        plannerRepository.delete(entity);
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        plannerRepository.deleteByMemberId(memberId);
    }
}

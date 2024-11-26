package com.yeojeong.application.domain.planner.planner.application.plannerfacade.implement;

import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.planner.location.application.locationservice.LocationService;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.application.plannerfacade.PlannerFacade;
import com.yeojeong.application.domain.planner.planner.application.plannerservice.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerRequest;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlannerFacadeImpl implements PlannerFacade {
    private final PlannerService plannerService;
    private final LocationService locationService;

    @Override
    public PlannerResponse.FindById save(PlannerRequest.Save dto) {
        Planner entity = PlannerRequest.Save.toEntity(dto);
        Planner savedEntity = plannerService.save(entity);

        return PlannerResponse.FindById.toDto(savedEntity);
    }

    @Override
    public PlannerResponse.FindById update(Long id, PlannerRequest.Put dto) {
        Planner savedEntity = plannerService.findById(id);

        if (locationService.existBefore(dto.startYear(), dto.startMonth(), dto.startDay(), dto.startHour(), dto.startMinute())){
            throw new RequestDataException("시작 날짜 변경 : Planner 와 관련된 Location 정보가 존재합니다.");
        }
        if (locationService.existAfter(dto.endYear(), dto.endMonth(), dto.endDay(), dto.endHour(), dto.endMinute())){
            throw new RequestDataException("끝나는 날짜 변경 : Planner 와 관련된 Location 정보가 존재합니다.");
        }

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

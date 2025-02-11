package com.yeojeong.application.domain.planner.location.application;

import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.member.application.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import com.yeojeong.application.domain.planner.planner.application.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationFacade {

    private final MemberService memberService;
    private final PlannerService plannerService;
    private final LocationService locationService;

    @Transactional
    public LocationResponse.FindById save(LocationRequest.Save dto, Long plannerId, Long memberId) {
        Member member = memberService.findById(memberId);
        Planner planner = plannerService.findById(plannerId);

        if (planner.getLocationCount() >= 15)
            throw new RequestDataException("Location 은 15개까지 생성 가능합니다.");

        Location entity = LocationRequest.Save.toEntity(dto, planner, member);
        Location savedEntity = locationService.save(entity);
        plannerService.updateLocation(planner);

        return LocationResponse.FindById.toDto(savedEntity);
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        Location savedEntity = locationService.findByIdAuth(id, memberId);

        Planner planner = plannerService.findById(savedEntity.getPlanner().getId());
        locationService.delete(savedEntity);
        plannerService.updateLocation(planner);
    }

    @Transactional
    public LocationResponse.FindById update(LocationRequest.Put dto, Long id, Long memberId) {
        Location savedEntity = locationService.findByIdAuth(id, memberId);

        Location updateEntity = LocationRequest.Put.toEntity(dto);
        Location rtnEntity = locationService.update(savedEntity, updateEntity);

        return LocationResponse.FindById.toDto(rtnEntity);
    }

    public LocationResponse.FindById findById(Long id) {
        Location saveEntity = locationService.findById(id);
        return LocationResponse.FindById.toDto(saveEntity);
    }
}

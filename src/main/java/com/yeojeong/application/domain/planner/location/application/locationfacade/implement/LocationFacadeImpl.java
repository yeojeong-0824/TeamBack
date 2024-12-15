package com.yeojeong.application.domain.planner.location.application.locationfacade.implement;

import com.yeojeong.application.config.exception.OwnershipException;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.application.locationfacade.LocationFacade;
import com.yeojeong.application.domain.planner.location.application.locationservice.LocationService;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import com.yeojeong.application.domain.planner.planner.application.plannerservice.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationFacadeImpl implements LocationFacade {

    private final MemberService memberService;
    private final PlannerService plannerService;
    private final LocationService locationService;

    @Override
    @Transactional
    public LocationResponse.FindById save(LocationRequest.Save dto, Long plannerId, Long memberId) {
        Member member = memberService.findById(memberId);
        Planner planner = plannerService.findById(plannerId);
        if (planner.getLocationCount() >= 15) throw new RequestDataException("Location 은 15개까지 생성 가능합니다.");

        Location entity = LocationRequest.Save.toEntity(dto, planner, member);
        Location savedEntity = locationService.save(entity);

        planner.addLocation();
        plannerService.save(planner);

        return LocationResponse.FindById.toDto(savedEntity);
    }

    @Override
    @Transactional
    public void delete(Long id, Long memberId) {
        Location savedEntity = locationService.findById(id);
        checkMember(savedEntity, memberId);

        Planner planner = plannerService.findById(savedEntity.getPlanner().getId());
        locationService.delete(savedEntity);

        planner.deleteLocation();
        plannerService.save(planner);
    }

    @Override
    @Transactional
    public LocationResponse.FindById update(LocationRequest.Put dto, Long id, Long memberId) {
        Location savedEntity = locationService.findById(id);
        checkMember(savedEntity, memberId);

        Location entity = LocationRequest.Put.toEntity(dto);
        savedEntity.update(entity);

        Location rtnEntity = locationService.update(savedEntity);
        return LocationResponse.FindById.toDto(rtnEntity);
    }

    @Override
    public LocationResponse.FindById findById(Long id, Long memberId) {
        Location saveEntity = locationService.findById(id);
        checkMember(saveEntity, memberId);

        return LocationResponse.FindById.toDto(saveEntity);
    }

    @Override
    public List<LocationResponse.FindById> findByPlannerId(Long plannerId, Long memberId) {
        List<Location> locationList = locationService.findByPlannerId(plannerId, memberId);

        return locationList.stream()
                .map(LocationResponse.FindById::toDto)
                .collect(Collectors.toList());
    }

    private void checkMember(Location location, Long memberId) {
        if (!memberId.equals(location.getMember().getId())) throw new OwnershipException("게시글을 작성한 사용자가 아닙니다.");
    }
}

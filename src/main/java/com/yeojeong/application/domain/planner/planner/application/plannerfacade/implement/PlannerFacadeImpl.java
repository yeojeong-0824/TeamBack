package com.yeojeong.application.domain.planner.planner.application.plannerfacade.implement;

import com.yeojeong.application.config.exception.OwnershipException;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
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

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlannerFacadeImpl implements PlannerFacade {
    private final MemberService memberService;
    private final PlannerService plannerService;
    private final LocationService locationService;

    @Override
    public PlannerResponse.FindById save(PlannerRequest.Save dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Planner entity = PlannerRequest.Save.toEntity(dto, member);
        Planner savedEntity = plannerService.save(entity);

        return PlannerResponse.FindById.toDto(savedEntity);
    }

    @Override
    public PlannerResponse.FindById update(Long id, PlannerRequest.Put dto, Long memberId) {
        Planner savedEntity = plannerService.findById(id);
        checkMember(savedEntity, memberId);

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
    public void delete(Long id, Long memberId) {
        Planner savedEntity = plannerService.findById(id);
        checkMember(savedEntity, memberId);

        plannerService.delete(savedEntity);
    }

    @Override
    public PlannerResponse.FindById findById(Long id, Long memberId) {
        Planner savedEntity = plannerService.findById(id);
        checkMember(savedEntity, memberId);
        return PlannerResponse.FindById.toDto(savedEntity);
    }

    private void checkMember(Planner planner, Long memberId) {
        if(!Objects.equals(memberId, planner.getMember().getId())) throw new OwnershipException("플레너를 작성한 사용자가 아닙니다.");
    }
}

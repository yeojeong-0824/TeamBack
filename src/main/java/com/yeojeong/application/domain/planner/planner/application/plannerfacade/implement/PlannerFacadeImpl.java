package com.yeojeong.application.domain.planner.planner.application.plannerfacade.implement;

import com.yeojeong.application.config.exception.OwnershipException;
import com.yeojeong.application.domain.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.application.locationservice.LocationService;
import com.yeojeong.application.domain.planner.planner.application.plannerfacade.PlannerFacade;
import com.yeojeong.application.domain.planner.planner.application.plannerservice.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerRequest;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlannerFacadeImpl implements PlannerFacade {
    private final MemberService memberService;
    private final PlannerService plannerService;
    private final LocationService locationService;

    @Override
    @Transactional
    public PlannerResponse.FindById save(PlannerRequest.Save dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Planner entity = PlannerRequest.Save.toEntity(dto, member);
        Planner savedEntity = plannerService.save(entity);

        return PlannerResponse.FindById.toDto(savedEntity);
    }

    @Override
    @Transactional
    public PlannerResponse.FindById update(Long id, PlannerRequest.Put dto, Long memberId) {
        Planner savedEntity = plannerService.findById(id);
        checkMember(savedEntity, memberId);

        Planner updateEntity = PlannerRequest.Put.toEntity(dto);
        Planner rtnEntity = plannerService.update(savedEntity, updateEntity);

        return PlannerResponse.FindById.toDto(rtnEntity);
    }

    @Override
    @Transactional
    public void delete(Long id, Long memberId) {
        Planner savedEntity = plannerService.findById(id);
        checkMember(savedEntity, memberId);
        plannerService.delete(savedEntity);
    }

    @Override
    public PlannerResponse.FindById findById(Long id) {
        Planner savedEntity = plannerService.findById(id);
        return PlannerResponse.FindById.toDto(savedEntity);
    }

    private void checkMember(Planner planner, Long memberId) {
        if(!Objects.equals(memberId, planner.getMember().getId())) throw new OwnershipException("플레너를 작성한 사용자가 아닙니다.");
    }
}

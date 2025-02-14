package com.yeojeong.application.domain.planner.planner.application;

import com.yeojeong.application.domain.member.application.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerRequest;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannerFacade {
    private final MemberService memberService;
    private final PlannerService plannerService;

    @Transactional
    public PlannerResponse.PlannerFindById save(PlannerRequest.PlannerSave dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Planner entity = PlannerRequest.PlannerSave.toEntity(dto, member);
        Planner savedEntity = plannerService.save(entity);

        return PlannerResponse.PlannerFindById.toDto(savedEntity);
    }

    @Transactional
    public PlannerResponse.PlannerFindById update(Long id, PlannerRequest.PlannerPut dto, Long memberId) {
        Planner savedEntity = plannerService.findByIdAuth(id, memberId);

        Planner updateEntity = PlannerRequest.PlannerPut.toEntity(dto);
        Planner rtnEntity = plannerService.update(savedEntity, updateEntity);

        return PlannerResponse.PlannerFindById.toDto(rtnEntity);
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        Planner savedEntity = plannerService.findByIdAuth(id, memberId);
        plannerService.delete(savedEntity);
    }

    public PlannerResponse.PlannerFindById findById(Long id) {
        Planner savedEntity = plannerService.findById(id);
        return PlannerResponse.PlannerFindById.toDto(savedEntity);
    }
}

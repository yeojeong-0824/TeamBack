package com.yeojeong.application.domain.planner.planner.application;

import com.yeojeong.application.config.exception.OwnershipException;
import com.yeojeong.application.domain.member.application.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.planner.location.application.LocationService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerRequest;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlannerFacade {
    private final MemberService memberService;
    private final PlannerService plannerService;

    @Transactional
    public PlannerResponse.FindById save(PlannerRequest.Save dto, Long memberId) {
        Member member = memberService.findById(memberId);
        Planner entity = PlannerRequest.Save.toEntity(dto, member);
        Planner savedEntity = plannerService.save(entity);

        return PlannerResponse.FindById.toDto(savedEntity);
    }

    @Transactional
    public PlannerResponse.FindById update(Long id, PlannerRequest.Put dto, Long memberId) {
        Planner savedEntity = plannerService.findByIdAuth(id, memberId);

        Planner updateEntity = PlannerRequest.Put.toEntity(dto);
        Planner rtnEntity = plannerService.update(savedEntity, updateEntity);

        return PlannerResponse.FindById.toDto(rtnEntity);
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        Planner savedEntity = plannerService.findByIdAuth(id, memberId);
        plannerService.delete(savedEntity);
    }

    public PlannerResponse.FindById findById(Long id) {
        Planner savedEntity = plannerService.findById(id);
        return PlannerResponse.FindById.toDto(savedEntity);
    }
}

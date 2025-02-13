package com.yeojeong.application.domain.planner.planner.presentation;

import com.yeojeong.application.config.doc.ResponseDoc;
import com.yeojeong.application.config.doc.StatusCreateDoc;
import com.yeojeong.application.config.doc.StatusNoContentDoc;
import com.yeojeong.application.config.doc.StatusOkDoc;
import com.yeojeong.application.domain.planner.planner.application.PlannerFacade;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerRequest;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import com.yeojeong.application.security.config.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/planners/authed")
@Tag(name = "플래너 API")
public class AuthedPlannerController {

    private final PlannerFacade plannerFacade;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "플래너 작성", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusCreateDoc
    public ResponseEntity<PlannerResponse.PlannerFindById> save(@Valid @RequestBody PlannerRequest.PlannerSave dto){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.CREATED).body(plannerFacade.save(dto, memberId));
    }

    @PutMapping(value ="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "플래너 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusOkDoc
    public ResponseEntity<PlannerResponse.PlannerFindById> update(@Valid @RequestBody PlannerRequest.PlannerPut dto, @PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(plannerFacade.update(id, dto, memberId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "플래너 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusNoContentDoc
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        plannerFacade.delete(id, memberId);
        return ResponseEntity.noContent().build();
    }
}

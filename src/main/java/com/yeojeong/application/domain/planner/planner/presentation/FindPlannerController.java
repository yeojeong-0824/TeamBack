package com.yeojeong.application.domain.planner.planner.presentation;

import com.yeojeong.application.config.doc.ResponseDoc;
import com.yeojeong.application.config.doc.StatusOkDoc;
import com.yeojeong.application.domain.planner.planner.application.PlannerFacade;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/planners")
@Tag(name = "플래너 조회 API")
public class FindPlannerController {

    private final PlannerFacade plannerFacade;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "플래너 호출")
    @ResponseDoc @StatusOkDoc
    public ResponseEntity<PlannerResponse.PlannerFindById> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(plannerFacade.findById(id));
    }
}

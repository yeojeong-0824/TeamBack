package com.yeojeong.application.domain.planner.planner.presentation;

import com.yeojeong.application.config.util.customannotation.MethodTimer;
import com.yeojeong.application.domain.planner.planner.application.plannerfacade.PlannerFacade;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import com.yeojeong.application.security.config.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @MethodTimer(method = "플래너 호출")
    @GetMapping("/{id}")
    @Operation(summary = "플래너 호출")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "플래너 호출 성공"),
                    @ApiResponse(responseCode = "400", description = "플래너 호출 실패")
            }
    )
    public ResponseEntity<PlannerResponse.FindById> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(plannerFacade.findById(id));
    }
}

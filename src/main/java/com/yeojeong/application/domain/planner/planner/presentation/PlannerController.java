package com.yeojeong.application.domain.planner.planner.presentation;

import com.yeojeong.application.config.util.customannotation.MethodTimer;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerRequest;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/planners")
@Tag(name = "플래너 API")
public class PlannerController {
    @MethodTimer(method = "플래너 작성")
    @PostMapping
    @Operation(summary = "플래너 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "플래너 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "플래너 작성 실패")
            }
    )
    public ResponseEntity<PlannerResponse.FindById> save(
            @Valid @RequestBody PlannerRequest.Save dto
    ){
        return null;
    }

    @MethodTimer(method = "플래너 수정")
    @PutMapping("/{id}")
    @Operation(summary = "플래너 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "플래너 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "플래너 수정 실패")
            }
    )
    public ResponseEntity<PlannerResponse.FindById> update(
            @Valid @RequestBody PlannerRequest.Put dto,
            @PathVariable("id") Long id){
        return null;
    }

    @MethodTimer(method = "플래너 삭제")
    @DeleteMapping("/{id}")
    @Operation(summary = "플래너 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "플래너 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "플래너 삭제 실패")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        return null;
    }
}

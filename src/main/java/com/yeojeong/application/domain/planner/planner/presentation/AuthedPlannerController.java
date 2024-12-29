package com.yeojeong.application.domain.planner.planner.presentation;

import com.yeojeong.application.domain.planner.planner.application.plannerfacade.PlannerFacade;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerRequest;
import com.yeojeong.application.domain.planner.planner.presentation.dto.PlannerResponse;
import com.yeojeong.application.security.config.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping
    @Operation(summary = "플래너 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    public ResponseEntity<PlannerResponse.FindById> save(
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @Valid @RequestBody PlannerRequest.Save dto
    ){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.CREATED).body(plannerFacade.save(dto, memberId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "플래너 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "플래너 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "플래너 수정 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "403", description = "서버 오류")
            }
    )
    public ResponseEntity<PlannerResponse.FindById> update(
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @Valid @RequestBody PlannerRequest.Put dto,
            @PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(plannerFacade.update(id, dto, memberId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "플래너 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "플래너 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "플래너 삭제 실패")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        plannerFacade.delete(id, memberId);
        return ResponseEntity.noContent().build();
    }
}

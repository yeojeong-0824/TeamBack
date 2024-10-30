package com.yeojeong.application.domain.planner.planner.presentation;

import com.yeojeong.application.config.util.customannotation.MethodTimer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    @ApiResponse(responseCode = "400", description = "플래너 작성 실패"),
            }
    )
    public ResponseEntity<String> save() {
        return null;
    }


}

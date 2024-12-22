package com.yeojeong.application.domain.planner.location.presentation;

import com.yeojeong.application.domain.planner.location.application.locationfacade.LocationFacade;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/planners/locations")
@Tag(name = "장소 조회 API")
public class FindLocationController {

    private final LocationFacade locationFacade;

    @GetMapping("/planners/{plannerId}")
    @Operation(summary = "플래너에 대한 장소를 조회", description = "Planner의 장소를 모두 조회합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "플래너에 대한 장소 조회 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<List<LocationResponse.FindById>> findByPlannerId (@PathVariable("plannerId") Long plannerId) {
        return ResponseEntity.status(HttpStatus.OK).body(locationFacade.findByPlannerId(plannerId));
    }
}

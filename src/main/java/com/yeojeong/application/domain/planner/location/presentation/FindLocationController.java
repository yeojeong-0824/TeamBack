package com.yeojeong.application.domain.planner.location.presentation;

import com.yeojeong.application.domain.planner.location.application.locationfacade.LocationFacade;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/planners/locations")
@Tag(name = "장소 조회 API")
public class FindLocationController {

    private final LocationFacade locationFacade;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "장소 호출", description = "Location의 장소를 호출합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "장소 조회 완료")
            }
    )
    public ResponseEntity<LocationResponse.FindById> put(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(locationFacade.findById(id));
    }
}

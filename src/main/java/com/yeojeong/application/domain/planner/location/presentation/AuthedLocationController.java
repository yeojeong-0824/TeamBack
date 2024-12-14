package com.yeojeong.application.domain.planner.location.presentation;

import com.yeojeong.application.config.util.customannotation.MethodTimer;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/planners/locations/authed")
@Tag(name = "장소 API")
public class AuthedLocationController {

    private final LocationFacade locationFacade;


    @MethodTimer(method = " 장소 작성")
    @PostMapping("/{plannerId}")
    @Operation(summary = "장소 작성", description = "Planner 의 장소를 기록합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "장소 등록 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<LocationResponse.FindById> save(@PathVariable("plannerId") Long plannerId,
                                                         @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                         @Valid @RequestBody LocationRequest.Save dto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.CREATED).body(locationFacade.save(dto, plannerId, memberId));
    }

    @MethodTimer(method = " 장소 수정")
    @PutMapping("/{id}")
    @Operation(summary = "장소 수정", description = "Planner의 장소를 수정합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "장소 수정 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<LocationResponse.FindById> put(@PathVariable("id") Long id,
                                                          @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                          @Valid @RequestBody LocationRequest.Put dto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(locationFacade.update(dto, id, memberId));
    }

    @MethodTimer(method = " 장소 삭제")
    @DeleteMapping("/{id}")
    @Operation(summary = "장소를 삭제", description = "Planner의 장소를 삭제합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "장소 삭제 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        locationFacade.delete(id, memberId);
        return ResponseEntity.noContent().build();
    }

    @MethodTimer(method = " 장소 조회")
    @GetMapping("/{id}")
    @Operation(summary = "장소를 조회", description = "Planner의 장소를 조회합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "장소 조회 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<LocationResponse.FindById> findById(@PathVariable("id") Long id) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(locationFacade.findById(id, memberId));
    }

    @MethodTimer(method = "플래너에 작성된 장소 조회")
    @GetMapping("/planners/{plannerId}")
    @Operation(summary = "플래너에 대한 장소를 조회", description = "Planner의 장소를 모두 조회합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "플래너에 대한 장소 조회 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<List<LocationResponse.FindById>> findByPlannerId (@PathVariable("plannerId") Long plannerId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(locationFacade.findByPlannerId(plannerId, memberId));
    }
}

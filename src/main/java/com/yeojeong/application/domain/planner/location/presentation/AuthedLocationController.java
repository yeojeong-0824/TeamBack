package com.yeojeong.application.domain.planner.location.presentation;

import com.yeojeong.application.config.doc.ResponseDoc;
import com.yeojeong.application.config.doc.StatusCreateDoc;
import com.yeojeong.application.config.doc.StatusNoContentDoc;
import com.yeojeong.application.config.doc.StatusOkDoc;
import com.yeojeong.application.domain.planner.location.application.LocationFacade;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationRequest;
import com.yeojeong.application.domain.planner.location.presentation.dto.LocationResponse;
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
@RequestMapping("/planners/locations/authed")
@Tag(name = "장소 API")
public class AuthedLocationController {

    private final LocationFacade locationFacade;


    @PostMapping(value = "/{plannerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "장소 작성", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusCreateDoc
    public ResponseEntity<LocationResponse.FindById> save(@PathVariable("plannerId") Long plannerId,
                                                          @Valid @RequestBody LocationRequest.LocationSave dto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.CREATED).body(locationFacade.save(dto, plannerId, memberId));
    }

    @PutMapping(value ="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "장소 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusOkDoc
    public ResponseEntity<LocationResponse.FindById> put(@PathVariable("id") Long id,
                                                         @Valid @RequestBody LocationRequest.LocationPut dto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(locationFacade.update(dto, id, memberId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "장소를 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusNoContentDoc
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        locationFacade.delete(id, memberId);
        return ResponseEntity.noContent().build();
    }
}

package com.yeojeong.application.domain.planner.location.presentation;

import com.yeojeong.application.config.util.customannotation.MethodTimer;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentRequest;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/locations/authed")
@Tag(name = "장소 API")
public class AuthedLocationController {
    @MethodTimer(method = " 장소 작성 호출")
    @PostMapping("/{plannerId}")
    @Operation(summary = "장소를 작성합니다.", description = "Planner의 장소를 기록합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "장소 등록 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<LocationResponse.FindById> save(@PathVariable("plannerId") Long plannerId,
                                                 @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                         @Valid @RequestBody LocationRequest.Save dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
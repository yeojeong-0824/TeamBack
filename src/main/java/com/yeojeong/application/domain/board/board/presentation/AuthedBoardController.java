package com.yeojeong.application.domain.board.board.presentation;

import com.yeojeong.application.domain.board.board.application.boardfacade.BoardFacade;
import com.yeojeong.application.domain.board.board.application.imagefacade.ImageFacade;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.security.config.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards/authed")
@Tag(name = "게시글 API (Authed)")
public class AuthedBoardController {

    private final BoardFacade boardFacade;
    private final ImageFacade imageFacade;

    @PostMapping
    @Operation(summary = "게시글 작성", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 작성 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<BoardResponse.FindById> save(
            @Valid @RequestBody BoardRequest.Save dto
    ){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.CREATED).body(boardFacade.save(dto, memberId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 수정 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<BoardResponse.FindById> update(
            @Valid @RequestBody BoardRequest.Put dto,
            @PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(boardFacade.update(id, memberId, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 삭제 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        boardFacade.delete(id, memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/images")
    @Operation(summary = "이미지 저장", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "이미지 저장 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 저장 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<String> images(@RequestPart(value = "image", required = false) @NonNull MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageFacade.saveImage(image));
    }
}

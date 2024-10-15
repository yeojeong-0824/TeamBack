package com.yeojeong.application.domain.board.board.presentation;

import com.yeojeong.application.domain.board.board.application.boardfacade.BoardFacade;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.config.util.customannotation.MethodTimer;
import com.yeojeong.application.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards/authed")
@Tag(name = "게시글 API (Authed)")
public class AuthedBoardController {

    private final BoardFacade boardFacade;

    @MethodTimer(method = "게시글 작성")
    @PostMapping
    @Operation(summary = "게시글 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 작성 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<BoardResponse.FindBoard> save(
            @Valid @RequestBody BoardRequest.SaveBoard dto
    ){
        Long memberId = SecurityUtil.getCurrentMemberId();

        return ResponseEntity.status(HttpStatus.CREATED).body(boardFacade.save(dto, memberId));
    }

    @MethodTimer(method = "게시글 수정")
    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 수정 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<BoardResponse.FindBoard> update(
            @Valid @RequestBody BoardRequest.PutBoard dto,
            @PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(boardFacade.update(id, memberId, dto));
    }

    @MethodTimer(method = "게시글 삭제")
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 삭제 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        boardFacade.delete(id, memberId);
        return ResponseEntity.ok("게시글 삭제 성공");
    }
}

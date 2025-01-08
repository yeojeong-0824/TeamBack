package com.yeojeong.application.domain.board.comment.presentation;

import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.comment.application.commentfacade.CommentFacade;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentRequest;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import com.yeojeong.application.security.config.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/boards/comments/authed")
@Tag(name = "댓글 API (Authed)")
public class AuthedCommentController {

    private final CommentFacade commentFacade;

    @PostMapping("/{boardId}")
    @Operation(summary = "댓글 등록", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "댓글 등록 완료"),
                    @ApiResponse(responseCode = "400", description = "게시글 수정 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<CommentResponse.FindById> save(@PathVariable("boardId") Long boardId,
                                                         @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                            @Valid @RequestBody CommentRequest.Save dto) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.CREATED).body(commentFacade.save(dto, boardId, memberId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<CommentResponse.FindById> path(@PathVariable("id") Long id,
                                                         @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                            @Valid @RequestBody CommentRequest.Put dto) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(commentFacade.update(id, memberId, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<BoardResponse.FindById> delete(@PathVariable("id") Long id) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(commentFacade.delete(id, memberId));
    }
}

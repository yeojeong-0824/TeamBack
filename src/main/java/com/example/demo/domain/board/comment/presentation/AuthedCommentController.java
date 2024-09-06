package com.example.demo.domain.board.comment.presentation;

import com.example.demo.domain.board.comment.application.commentservice.CommentService;
import com.example.demo.domain.board.comment.presentation.dto.CommentRequest;
import com.example.demo.config.util.customannotation.MethodTimer;
import com.example.demo.domain.board.comment.presentation.dto.CommentResponse;
import com.example.demo.security.SecurityUtil;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board/comment/authed")
@PreAuthorize("isAuthenticated()")
@Tag(name = "댓글 API (Authed)")
public class AuthedCommentController {

    private final CommentService commentService;

    @MethodTimer(method = "댓글 작성 호출")
    @PostMapping("/{boardId}")
    @Operation(summary = "댓글 등록", description = "게시글에 댓글을 기록합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 등록 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<CommentResponse.FindComment> save(@PathVariable("boardId") Long boardId,
                                                            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                       @Valid @RequestBody CommentRequest.Save takenDto) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(takenDto, boardId, memberId));
    }

    @MethodTimer(method = "댓글 수정 호출")
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "게시글 댓글을 수정합니다")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<CommentResponse.FindComment> edit(@PathVariable("commentId") Long commentId,
                                       @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                       @Valid @RequestBody CommentRequest.Edit takenDto) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(commentService.updateById(commentId, memberId, takenDto));
    }

    @MethodTimer(method = "댓글 삭제 호출")
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "게시글 댓글을 삭제합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<CommentResponse.DeleteComment> delete(@PathVariable("commentId") Long commentId) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(commentService.deleteById(commentId, memberId));
    }
}

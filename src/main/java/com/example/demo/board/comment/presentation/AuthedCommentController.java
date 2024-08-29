package com.example.demo.board.comment.presentation;

import com.example.demo.board.comment.application.CommentService;
import com.example.demo.board.comment.presentation.dto.CommentRequest;
import com.example.demo.config.util.customannotation.MethodTimer;
import com.example.demo.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<String> save(
            @PathVariable("boardId") Long boardId,
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @Valid @RequestBody CommentRequest.Save takenDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        commentService.save(takenDto, boardId, memberId);
        return ResponseEntity.ok("댓글 등록에 성공하였습니다");
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
    public ResponseEntity<String> edit(
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentRequest.Edit takenDto){
        Long memberId = SecurityUtil.getCurrentMemberId();
        commentService.updateById(commentId, memberId, takenDto);
        return ResponseEntity.ok("댓글 수정에 성공하였습니다");
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
    public ResponseEntity<String> delete(
            @PathVariable("commentId") Long commentId){
        Long memberId = SecurityUtil.getCurrentMemberId();
        commentService.deleteById(commentId, memberId);
        return ResponseEntity.ok("댓글 삭제에 성공하였습니다");
    }
}

package com.example.demo.board.comment.presentation;

import com.example.demo.board.comment.application.CommentService;
import com.example.demo.board.comment.application.CommentServiceImpl;
import com.example.demo.board.comment.presentation.dto.CommentRequest.CommentSaveRequest;
import com.example.demo.board.comment.presentation.dto.CommentRequest.CommentUpdateRequest;
import com.example.demo.board.comment.presentation.dto.CommentResponse;
import com.example.demo.config.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
// Swagger 문서 정리하면서 url /board/comment로 변경했어요!
// 확인해보시고 문제가 될 것 같으면 말씀주세요!
@RequestMapping("/board/comment/authed")
@PreAuthorize("isAuthenticated()")
@Tag(name = "댓글 API (Authed)")
public class AuthedCommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    @Operation(summary = "댓글 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "댓글 작성 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<String> save(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentSaveRequest request
    ){
        Long memberId = SecurityUtil.getCurrentMemberId();
        commentService.save(memberId, boardId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("댓글 작성 완료");
    }

    @PutMapping("/update/{commentId}")
    @Operation(summary = "댓글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "댓글 수정 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<String> updateById(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        commentService.updateById(memberId, commentId, request);
        return ResponseEntity.ok("댓글 수정 완료");
    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "댓글 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "댓글 삭제 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<String> deleteById(@PathVariable Long commentId){
        Long memberId = SecurityUtil.getCurrentMemberId();
        commentService.deleteById(memberId, commentId);
        return ResponseEntity.ok("댓글 삭제 완료");
    }
}

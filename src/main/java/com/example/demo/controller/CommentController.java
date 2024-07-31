package com.example.demo.controller;

import com.example.demo.dto.comment.CommentRequest.*;
import com.example.demo.dto.comment.CommentResponse.*;
import com.example.demo.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@Tag(name = "댓글 API (Authed)")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{boardId}")
    @Operation(summary = "댓글 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "댓글 작성 실패")
            }
    )
    public ResponseEntity<CommentSaveResponse> commentWrite(
            @PathVariable Long boardId,
            @RequestBody CommentSaveRequest request
    ){
        // TODO : username 을 찾는 코드 작성 필요
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.writeComment(request, boardId, ""));
    }

    @PutMapping("/update/{commentId}")
    @Operation(summary = "댓글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "댓글 수정 실패")
            }
    )
    public ResponseEntity<CommentSaveResponse> commentUpdate(
            @PathVariable Long commentId,
            @RequestBody CommentSaveRequest request
    ) {
        return ResponseEntity.ok(commentService.updateComment(request, commentId));
    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "댓글 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "댓글 삭제 실패")
            }
    )
    public ResponseEntity<Long> commentDelete(@PathVariable Long commentId){
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}

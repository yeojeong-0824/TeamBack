package com.example.demo.board.comment.presentation;

import com.example.demo.board.comment.application.CommentService;
import com.example.demo.board.comment.presentation.dto.CommentRequest.*;
import com.example.demo.board.comment.presentation.dto.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> save(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentSaveRequest request
    ){
        // TODO : username 을 찾는 코드 작성 필요
        commentService.save(boardId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("댓글 작성 완료");
    }

    @PutMapping("/update/{commentId}")
    @Operation(summary = "댓글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "댓글 수정 실패")
            }
    )
    public ResponseEntity<String> update(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        commentService.update(commentId, request);
        return ResponseEntity.ok("댓글 수정 완료");
    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "댓글 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "댓글 삭제 실패")
            }
    )
    public ResponseEntity<String> delete(@PathVariable Long commentId){
        commentService.delete(commentId);
        return ResponseEntity.ok("댓글 삭제 완료");
    }
}

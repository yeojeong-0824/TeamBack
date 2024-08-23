package com.example.demo.board.comment.presentation;

import com.example.demo.board.comment.application.CommentService;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.board.comment.presentation.dto.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board/comment")
@Tag(name = "댓글 API")
public class FindCommentController {
    private final CommentService commentService;

    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 별 댓글 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "댓글 조회 실패")
            }
    )
    public ResponseEntity<List<CommentResponse.CommentListResponse>> findByBoardId(@PathVariable Long boardId){
        return ResponseEntity.ok(commentService.findByBoardId(boardId));
    }
}

package com.example.demo.domain.board.comment.presentation;

import com.example.demo.domain.board.comment.application.commentservice.CommentService;
import com.example.demo.domain.board.comment.presentation.dto.CommentResponse;
import com.example.demo.config.util.customannotation.MethodTimer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards/comments")
@Tag(name = "댓글 API")
public class FindCommentController {

    private final CommentService commentService;

    @MethodTimer(method = "게시글에 작성된 댓글 호출")
    @GetMapping("/{boardId}")
    @Operation(summary = "게시글에 작성된 댓글 호출", description = "게시글에 댓글 불러옵니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 등록 완료")
            }
    )
    public ResponseEntity<Page<CommentResponse.FindByBoardId>> findByBoardId(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                                                                             @PathVariable("boardId") Long boardId) {
        Page<CommentResponse.FindByBoardId> rtn = commentService.findByBoardId(boardId, page);
        return ResponseEntity.ok(rtn);
    }
}

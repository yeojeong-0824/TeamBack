package com.example.demo.board.comment.presentation;

import com.example.demo.board.comment.application.CommentService;
import com.example.demo.board.comment.presentation.dto.CommentRequest;
import com.example.demo.board.comment.presentation.dto.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board/comment")
@Tag(name = "댓글 API")
public class FindCommentController {

    private final CommentService commentService;
    @GetMapping("/{boardId}")
    @Operation(summary = "댓글 받기", description = "게시글에 댓글 불러옵니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "댓글 등록 완료")
            }
    )
    public ResponseEntity<Page<CommentResponse.FindByBoardId>> findByBoardId(@PathVariable("boardId") Long boardId,
                                                             HttpServletRequest request) {
        Page<CommentResponse.FindByBoardId> rtn = commentService.findByBoardId(boardId);
        return ResponseEntity.ok(rtn);
    }
}

package com.yeojeong.application.domain.board.comment.presentation;

import com.yeojeong.application.config.doc.ResponseDoc;
import com.yeojeong.application.config.doc.StatusOkDoc;
import com.yeojeong.application.domain.board.comment.application.CommentFacade;
import com.yeojeong.application.domain.board.comment.presentation.dto.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards/comments")
@Tag(name = "댓글 API")
public class FindCommentController {

    private final CommentFacade commentFacade;

    @GetMapping(value = "/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글에 작성된 댓글 호출", description = "게시글에 댓글 불러옵니다.")
    @ResponseDoc @StatusOkDoc
    public ResponseEntity<Page<CommentResponse.FindByBoardId>> findByBoardId(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                                                                             @PathVariable("boardId") Long boardId) {
        Page<CommentResponse.FindByBoardId> rtn = commentFacade.findByBoardId(boardId, page);
        return ResponseEntity.ok(rtn);
    }
}

package com.yeojeong.application.domain.board.comment.presentation;

import com.yeojeong.application.config.doc.ResponseDoc;
import com.yeojeong.application.config.doc.StatusCreateDoc;
import com.yeojeong.application.config.doc.StatusOkDoc;
import com.yeojeong.application.domain.board.board.application.boardfacade.BoardFacade;
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
    private final BoardFacade boardFacade;

    @PostMapping(value = "/{boardId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "댓글 등록", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusCreateDoc
    public ResponseEntity<Void> save(@PathVariable("boardId") Long boardId,
                                     @Valid @RequestBody CommentRequest.Save dto) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        commentFacade.save(dto, boardId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "댓글 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusOkDoc
    public ResponseEntity<Void> path(@PathVariable("id") Long id,
                                     @Valid @RequestBody CommentRequest.Put dto) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        commentFacade.update(id, memberId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "댓글 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusOkDoc
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        commentFacade.delete(id, memberId);
        return ResponseEntity.noContent().build();
    }
}

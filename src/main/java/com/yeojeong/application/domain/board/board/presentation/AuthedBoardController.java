package com.yeojeong.application.domain.board.board.presentation;

import com.yeojeong.application.config.doc.ResponseDoc;
import com.yeojeong.application.config.doc.StatusCreateDoc;
import com.yeojeong.application.config.doc.StatusNoContentDoc;
import com.yeojeong.application.config.doc.StatusOkDoc;
import com.yeojeong.application.domain.board.board.application.BoardFacade;
import com.yeojeong.application.domain.board.board.application.ImageFacade;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardRequest;
import com.yeojeong.application.security.config.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards/authed")
@Tag(name = "게시글 API (Authed)")
public class AuthedBoardController {

    private final BoardFacade boardFacade;
    private final ImageFacade imageFacade;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 작성", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusCreateDoc
    public ResponseEntity<BoardResponse.FindById> save(@Valid @RequestBody BoardRequest.BoardSave dto){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.CREATED).body(boardFacade.save(dto, memberId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusOkDoc
    public ResponseEntity<BoardResponse.FindById> update(@Valid @RequestBody BoardRequest.BoardPut dto, @PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(boardFacade.update(id, memberId, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusNoContentDoc
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        boardFacade.delete(id, memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이미지 저장", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusCreateDoc
    public ResponseEntity<BoardResponse.ImageUrl> images(@RequestPart(value = "image", required = false) @NonNull MultipartFile image) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.CREATED).body(imageFacade.save(image, memberId));
    }

    @DeleteMapping(value = "/images/{id}")
    @Operation(summary = "이미지 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseDoc @StatusCreateDoc
    public ResponseEntity<Void> imagesDel(@PathVariable("id") String id) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        imageFacade.delete(id, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

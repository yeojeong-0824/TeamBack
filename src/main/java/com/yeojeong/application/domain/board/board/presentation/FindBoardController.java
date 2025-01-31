package com.yeojeong.application.domain.board.board.presentation;

import com.yeojeong.application.config.doc.ResponseDoc;
import com.yeojeong.application.domain.board.board.application.boardfacade.BoardFacade;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.board.board.presentation.dto.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
@Tag(name = "게시글 찾기 API")
public class FindBoardController {

    private final BoardFacade boardFacade;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 호출")
    @ResponseDoc
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공")
            }
    )
    public ResponseEntity<BoardResponse.FindById> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(boardFacade.findById(id));
    }

    @GetMapping(value = "/commentsUpdate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "댓글 업데이트 후 호출")
    @ResponseDoc
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<BoardResponse.FindById> findByIdForComment(@PathVariable("id") Long id) {
        return ResponseEntity.ok(boardFacade.findByIdForComment(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "조건에 따른 게시글 검색, 정렬")
    @ResponseDoc
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<Page<BoardResponse.FindAll>> findAll(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sortKeyword", defaultValue = "latest") SortType sortType,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page){
        return ResponseEntity.ok(boardFacade.findAll(keyword, sortType, page));
    }
}

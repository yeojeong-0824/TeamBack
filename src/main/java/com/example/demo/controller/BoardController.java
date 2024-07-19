package com.example.demo.controller;

import com.example.demo.dto.board.BoardRequest.*;
import com.example.demo.dto.board.BoardResponse.*;
import com.example.demo.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "게시글 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 작성 실패")
            }
    )
    public ResponseEntity<BoardSaveResponse> boardWrite(@RequestBody BoardSaveRequest request){
        // TODO : username 을 찾는 코드 작성 필요
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.writeBoard(request));
    }

    @PutMapping("/update/{boardId}")
    @Operation(summary = "게시글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 수정 실패")
            }
    )
    public ResponseEntity<BoardUpdateResponse> boardUpdate(
            @RequestBody BoardUpdateRequest request,
            @PathVariable Long boardId
    ){
        return ResponseEntity.ok(boardService.updateBoard(boardId, request));
    }

    @DeleteMapping("/delete/{boardId}")
    @Operation(summary = "게시글 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 삭제 실패")
            }
    )
    public ResponseEntity<Long> boardDelete(@PathVariable Long boardId){
        return ResponseEntity.ok(boardService.deleteBoard(boardId));
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 호출")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 호출 성공"),
                    @ApiResponse(responseCode = "404", description = "존재하는 게시글 없음")
            }
    )
    public ResponseEntity<BoardReadResponse> getBoard(@PathVariable Long boardId){
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @GetMapping
    @Operation(summary = "조건에 따른 게시글 검색, 정렬")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 검색, 정렬 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 검색, 정렬 실패")
            }
    )
    public ResponseEntity<Page<BoardListResponse>> boardSearch(
            @RequestParam String keyword,
            @RequestParam String searchKeyword,
            @RequestParam String sortKeyword,
            @RequestParam(required = false, defaultValue = "1") int page){
        return ResponseEntity.ok(boardService.getSearchBoardList(searchKeyword, keyword, sortKeyword, page));
    }

}

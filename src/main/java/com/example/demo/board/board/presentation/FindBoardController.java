package com.example.demo.board.board.presentation;

import com.example.demo.board.board.presentation.dto.BoardResponse;
import com.example.demo.board.board.application.BoardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
@Tag(name = "게시글 찾기 API")
public class FindBoardController {
    private final BoardServiceImpl boardServiceImpl;

    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 호출")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 호출 성공"),
                    @ApiResponse(responseCode = "404", description = "존재하는 게시글 없음")
            }
    )
    public ResponseEntity<BoardResponse.BoardReadResponse> getBoard(@PathVariable Long boardId){
        return ResponseEntity.ok(boardServiceImpl.findById(boardId));
    }

    @GetMapping("/list")
    @Operation(summary = "모든 게시글")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "모든 게시글 검색 성공"),
                    @ApiResponse(responseCode = "400", description = "모든 게시글 검색 실패")
            }
    )
    public ResponseEntity<Page<BoardResponse.BoardListResponse>> boardList(
            @RequestParam(required = false, defaultValue = "1") int page){
        return ResponseEntity.ok(boardServiceImpl.findAll(page));
    }

    @GetMapping
    @Operation(summary = "조건에 따른 게시글 검색, 정렬")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 검색, 정렬 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 검색, 정렬 실패")
            }
    )
    public ResponseEntity<Page<BoardResponse.BoardListResponse>> boardSearch(
            @RequestParam String keyword,
            @RequestParam String searchKeyword,
            @RequestParam String sortKeyword,
            @RequestParam(required = false, defaultValue = "1") int page){
        return ResponseEntity.ok(boardServiceImpl.findAllBySearchKeyword(searchKeyword, keyword, sortKeyword, page));
    }
}

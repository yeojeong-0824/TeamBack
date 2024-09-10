package com.example.demo.domain.board.board.presentation;

import com.example.demo.domain.board.board.presentation.dto.BoardResponse;
import com.example.demo.domain.board.board.application.boardservice.implement.BoardServiceImpl;
import com.example.demo.config.util.customannotation.MethodTimer;
import com.example.demo.security.SecurityUtil;
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
@RequestMapping("/api/vi/boards")
@Tag(name = "게시글 찾기 API")
public class FindBoardController {
    private final BoardServiceImpl boardServiceImpl;

    @MethodTimer(method = "게시글 호출")
    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 호출")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 호출 성공"),
                    @ApiResponse(responseCode = "404", description = "존재하는 게시글 없음")
            }
    )
    public ResponseEntity<BoardResponse.FindBoard> getBoard(@PathVariable("boardId") Long boardId){
        Long memberId = SecurityUtil.getCurrentMemberId();

        return ResponseEntity.ok(boardServiceImpl.findById(boardId, memberId));
    }

    @MethodTimer(method = "조건에 따른 게시글 호출")
    @GetMapping
    @Operation(summary = "조건에 따른 게시글 검색, 정렬")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 검색, 정렬 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 검색, 정렬 실패")
            }
    )
    public ResponseEntity<Page<BoardResponse.FindBoardList>> boardSearch(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "searchKeyword") String searchKeyword,
            @RequestParam(value = "sortKeyword") String sortKeyword,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page){
        return ResponseEntity.ok(boardServiceImpl.findAllBySearchKeyword(searchKeyword, keyword, sortKeyword, page));
    }
}

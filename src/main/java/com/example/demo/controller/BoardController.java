package com.example.demo.controller;

import com.example.demo.dto.board.BoardCreateRequest;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardResponse;
import com.example.demo.entity.Board;
import com.example.demo.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {
    private BoardService boardService;

    // 게시글 작성
    @PostMapping("/write")
    public ResponseEntity<Board> boardWrite(@RequestBody BoardCreateRequest request){
        return ResponseEntity.ok(boardService.writeBoard(request, (short) 0));
    }

    // 게시글 수정
    @GetMapping("/update/{boardId}")
    public ResponseEntity<Board> boardUpdate(
            @RequestBody BoardRequest.BoardUpdateRequest request,
            @PathVariable Long boardId
    ){
        return ResponseEntity.ok(boardService.updateBoard(boardId, request));
    }

    // 게시글 삭제
    @GetMapping("/delete/{boardId}")
    public ResponseEntity<Long> boardDelete(@PathVariable Long boardId){
        return ResponseEntity.ok(boardService.deleteBoard(boardId));
    }

    // 하나의 게시글
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse.BoardReadResponse> getBoard(@PathVariable Long boardId){
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    // 조건에 따른 게시글 검색, 정렬
    @GetMapping("/search")
    public ResponseEntity<Page<Board>> boardSearch(
            @RequestParam String keyword,
            @RequestParam String searchKeyword,
            @RequestParam String sortKeyword,
            @RequestParam(required = false, defaultValue = "1") int page){
        return ResponseEntity.ok(boardService.getSearchBoardList(searchKeyword, keyword, sortKeyword, page));
    }
}

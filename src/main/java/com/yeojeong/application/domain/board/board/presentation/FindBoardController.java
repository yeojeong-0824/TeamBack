package com.yeojeong.application.domain.board.board.presentation;

import com.yeojeong.application.domain.board.board.application.boardfacade.BoardFacade;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.config.util.customannotation.MethodTimer;
import com.yeojeong.application.security.SecurityUtil;
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
@RequestMapping("/boards")
@Tag(name = "게시글 찾기 API")
public class FindBoardController {

    private final BoardFacade boardFacade;

    @MethodTimer(method = "게시글 호출")
    @GetMapping("/{id}")
    @Operation(summary = "게시글 호출")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 호출 성공"),
                    @ApiResponse(responseCode = "404", description = "존재하는 게시글 없음")
            }
    )
    public ResponseEntity<BoardResponse.FindById> findById(@PathVariable("id") Long id){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(boardFacade.findById(id, memberId));
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
    public ResponseEntity<Page<BoardResponse.FindAll>> findAll(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "searchKeyword") String searchKeyword,
            @RequestParam(value = "sortKeyword") String sortKeyword,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page){
        return ResponseEntity.ok(boardFacade.findAll(searchKeyword, keyword, sortKeyword, page));
    }
}

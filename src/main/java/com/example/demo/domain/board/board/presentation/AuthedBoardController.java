package com.example.demo.domain.board.board.presentation;

import com.example.demo.domain.board.board.application.boardservice.BoardService;
import com.example.demo.domain.board.board.presentation.dto.BoardResponse;
import com.example.demo.domain.board.board.presentation.dto.BoardRequest;
import com.example.demo.config.util.customannotation.MethodTimer;
import com.example.demo.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// 유저 정보 불러오기 오류 수정했습니다
// 게시글 작성까지 잘 되는 걸로 확인 했어요!
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards/authed")
@Tag(name = "게시글 API (Authed)")
@PreAuthorize("isAuthenticated()")
public class AuthedBoardController {
    private final BoardService boardService;

    // 통일성을 주기위해 메서드 명을 save로 바꿨습니다!
    @MethodTimer(method = "게시글 작성")
    @PostMapping
    @Operation(summary = "게시글 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 작성 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<BoardResponse.FindBoard> save(
            @Valid @RequestBody BoardRequest.SaveBoard request
    ){
        Long memberId = SecurityUtil.getCurrentMemberId();

        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.save(request, memberId));
    }

    @MethodTimer(method = "게시글 수정")
    @PutMapping("/{boardId}")
    @Operation(summary = "게시글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 수정 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<BoardResponse.FindBoard> boardUpdate(
            @Valid @RequestBody BoardRequest.PutBoard request,
            @PathVariable("boardId") Long boardId){
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(boardService.updateById(boardId, memberId, request));
    }

    @MethodTimer(method = "게시글 삭제")
    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시글 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 삭제 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<String> boardDelete(@PathVariable("boardId") Long boardId){
        Long memberId = SecurityUtil.getCurrentMemberId();
        boardService.deleteById(boardId, memberId);
        return ResponseEntity.ok("게시글 삭제 성공");
    }
}

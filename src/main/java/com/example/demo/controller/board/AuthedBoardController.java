package com.example.demo.controller.board;

import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardResponse;
import com.example.demo.dto.board.GoogleApiResponse;
import com.example.demo.dto.member.MemberDetails;
import com.example.demo.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// 유저 정보 불러오기 오류 수정했습니다
// 게시글 작성까지 잘 되는 걸로 확인 했어요!
@RequiredArgsConstructor
@RestController
@RequestMapping("/board/authed")
@Tag(name = "게시글 API (Authed)")
@PreAuthorize("isAuthenticated()")
public class AuthedBoardController {
    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "게시글 작성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 작성 실패")
            }
    )
    public ResponseEntity<BoardResponse.BoardSaveResponse> boardWrite(
            @Valid @RequestBody BoardRequest.BoardSaveRequest request
    ){
        MemberDetails memberDetails = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String memberName = memberDetails.getUsername();
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.writeBoard(request, memberName));
    }

    @GetMapping("/search")
    @Operation(summary = "구글 api 를 이용한 장소 검색")
    public ResponseEntity<GoogleApiResponse> locationSearch(@RequestParam String textQuery){
        return ResponseEntity.ok(boardService.getSearchLocation(textQuery));
    }

    @PutMapping("/update/{boardId}")
    @Operation(summary = "게시글 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "게시글 수정 실패")
            }
    )
    public ResponseEntity<BoardResponse.BoardUpdateResponse> boardUpdate(
            @Valid @RequestBody BoardRequest.BoardUpdateRequest request,
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
}

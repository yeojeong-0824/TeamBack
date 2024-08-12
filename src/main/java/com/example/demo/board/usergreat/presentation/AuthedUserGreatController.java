package com.example.demo.board.usergreat.presentation;

import com.example.demo.board.usergreat.application.UserGreatService;
import com.example.demo.config.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "별점 정보 API (Authed)")
@PreAuthorize("isAuthenticated()")
@RequestMapping("/board/userGreat/authed")
public class AuthedUserGreatController {

    private final UserGreatService userGreatService;

    @PostMapping("/{boardId}")
    @Operation(summary = "게시글 좋아요 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 재발급 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<String> save(@PathVariable("boardId") Long boardId) {
        Long userId = SecurityUtil.getCurrentUserId();
        userGreatService.save(userId, boardId);
        return ResponseEntity.ok("좋아요 누르기에 성공하였습니다");
    }
}

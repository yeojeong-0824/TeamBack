package com.example.demo.board.usergreat.presentation;

import com.example.demo.board.usergreat.application.UserGreatService;
import com.example.demo.board.usergreat.presentation.dto.UserGreatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "별점 정보 API")
@RequestMapping("/board/userGreat/authed")
public class UserGreatController {

    private final UserGreatService userGreatService;
    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 좋아요 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 재발급 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<List<UserGreatResponse.UserIdByBoardId>> findUserIdByBoardId(@PathVariable("boardId") Long boardId) {
        List<UserGreatResponse.UserIdByBoardId> rtn = userGreatService.findUserIdByBoardId(boardId);
        return ResponseEntity.ok(rtn);
    }
}

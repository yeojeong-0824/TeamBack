package com.example.demo.board.usergreat.presentation;

import com.example.demo.board.usergreat.application.UserGreatService;
import com.example.demo.board.usergreat.presentation.dto.UserGreatRequest;
import com.example.demo.config.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
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
    public ResponseEntity<String> save(@PathVariable("boardId") Long boardId,
                                       @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                       @RequestBody UserGreatRequest.SaveUserGreat takenDto) {

        Long userId = SecurityUtil.getCurrentUserId();

        userGreatService.save(takenDto, userId, boardId);
        return ResponseEntity.ok("댓글 생성에 성공하였습니다");
    }
}

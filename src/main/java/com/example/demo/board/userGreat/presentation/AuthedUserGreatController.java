package com.example.demo.board.userGreat.presentation;

import com.example.demo.board.userGreat.application.UserGreatService;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/like/authed")
public class AuthedUserGreatController {

    private final UserGreatService userGreatService;

    @PatchMapping("/{boardId}")
    @Operation(summary = "게시글 좋아요 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 재발급 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<String> save(MemberRequest.SaveMember takenDto) {
        return null;
    }
}

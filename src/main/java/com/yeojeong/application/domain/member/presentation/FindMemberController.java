package com.yeojeong.application.domain.member.presentation;

import com.yeojeong.application.domain.member.application.memberfacade.MemberFacade;
import com.yeojeong.application.domain.member.presentation.dto.MemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members/findMembers")
@Validated
@Tag(name = "유저 찾기 API")
public class FindMemberController {

    private final MemberFacade memberFacade;

    @PatchMapping(value = "/passwords", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "새로운 비밀번호 발급", description = "새로운 비밀번호를 발급합니다. 새로운 비밀번호는 해당 아이디의 이메일로 발송됩니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 재발급 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<Void> findPassword(@Valid @RequestBody MemberRequest.FindPassword dto) {
        memberFacade.findPassword(dto.username(), dto.email());
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/usernames/{email}")
    @Operation(summary = "아이디 찾기", description = "해당 이메일로 회원가입이 된 아이디가 존재한다면, 해당 이메일로 아이디를 발송합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "아이디 전송 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<Void> findUsername(@Size(min = 1, max = 50) @Email @PathVariable("email") String email) {

        memberFacade.findUsernameByEmail(email);
        return ResponseEntity.ok().build();
    }
}

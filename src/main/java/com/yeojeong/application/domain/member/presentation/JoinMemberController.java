package com.yeojeong.application.domain.member.presentation;

import com.yeojeong.application.config.doc.ResponseDoc;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
@Validated
@Tag(name = "회원가입 API")
public class JoinMemberController {

    private final MemberFacade memberFacade;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입", description = "회원을 생성합니다.")
    @ResponseDoc
    @ApiResponses(
        value = {
                @ApiResponse(responseCode = "201", description = "성공"),
        }
    )
    public ResponseEntity<Void> save(@Valid @RequestBody MemberRequest.SaveMember dto) {
        memberFacade.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/check/username/{username}")
    @Operation(summary = "아이디 중복 검사", description = "이미 사용되고 있는 아이디인지 확인합니다.")
    @ResponseDoc
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<Void> checkDuplicatedByUsername(@Size(min = 5, max = 30) @PathVariable("username") String username) {

        memberFacade.checkDuplicatedByUsername(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/nickname/{nickname}")
    @Operation(summary = "닉네임 중복 검사", description = "이미 사용되고 있는 닉네임인지 확인합니다.")
    @ResponseDoc
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<Void> checkDuplicatedByNickname(@Size(min = 1, max = 10) @PathVariable("nickname") String nickname) {

        memberFacade.checkDuplicatedByNickname(nickname);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/emailAuthed/{email}")
    @Operation(summary = "이메일 중복 확인 및 회원가입 인증 이메일 발송", description = "이메일이 중복되었는지 확인 후 중복되지 않았으면 인증 이메일을 발송합니다.")
    @ResponseDoc
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<Void> authedByEmail(@Size(min = 1, max = 50) @Email @PathVariable("email") String email) {

        memberFacade.checkDuplicatedByEmail(email);
        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/emailAuthed/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이메일 인증코드 확인", description = "이메일 인증을 시도합니다. 이메일 인증 이메일이 발송되지 않았으면 이메일 인증 실패를 하게 됩니다.")
    @ResponseDoc
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    public ResponseEntity<Void> authedCheckByEmail(@Size(min = 1, max = 50) @Email @PathVariable("email") String email,
                                                   @RequestBody MemberRequest.EmailAuthedKey dto) {
        memberFacade.checkAuthCheck(email, dto.key());
        return ResponseEntity.ok().build();
    }
}

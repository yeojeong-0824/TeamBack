package com.yeojeong.application.domain.member.member.presentation;

import com.yeojeong.application.config.util.customannotation.MethodTimer;
import com.yeojeong.application.domain.member.email.application.memberemailservice.MemberEmailService;
import com.yeojeong.application.domain.member.email.presentation.dto.SendEmail;
import com.yeojeong.application.domain.member.member.application.memberfacade.MemberFacade;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members/findMember")
@Validated
@Tag(name = "유저 찾기 API")
public class FindMemberController {

    private final MemberFacade memberFacade;
    private final MemberEmailService memberEmailService;

    @MethodTimer(method = "새로운 비밀번호 발급 호출")
    @PatchMapping("/passwords")
    @Operation(summary = "새로운 비밀번호 발급", description = "새로운 비밀번호를 발급합니다. 새로운 비밀번호는 해당 아이디의 이메일로 발송됩니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 재발급 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<Void> findPassword(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                               @Valid @RequestBody MemberRequest.FindPassword dto) {
        String newPassword = memberFacade.findPassword(dto.username(), dto.email());

        SendEmail.FindPassword sendDto = SendEmail.FindPassword.builder()
                .email(dto.email())
                .password(newPassword)
                .build();

        memberEmailService.sendFindPassword(sendDto);
        return ResponseEntity.ok().build();
    }


    @MethodTimer(method = "아이디 찾기 호출")
    @GetMapping("/usernames/{email}")
    @Operation(summary = "아이디 찾기", description = "해당 이메일로 회원가입이 된 아이디가 존재한다면, 해당 이메일로 아이디를 발송합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "아이디 전송 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<Void> findUsername(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                               @PathVariable("email") String email) {

        String username = memberFacade.findUsernameByEmail(email);

        SendEmail.FindUsername sendDto = SendEmail.FindUsername.builder()
                .email(email)
                .username(username)
                .build();

        memberEmailService.sendFindUsername(sendDto);
        return ResponseEntity.ok().build();
    }
}

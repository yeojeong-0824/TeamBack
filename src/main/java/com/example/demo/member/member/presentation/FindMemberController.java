package com.example.demo.member.member.presentation;

import com.example.demo.config.util.customannotation.MethodTimer;
import com.example.demo.member.member.application.MemberService;
import com.example.demo.member.email.application.FindMemberEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/findMember")
@Validated
@Tag(name = "유저 찾기 API")
public class FindMemberController {

    private final MemberService memberService;
    private final FindMemberEmailService findMemberEmailService;

    @MethodTimer(method = "새로운 비밀번호 발급 호출")
    @PatchMapping("/password")
    @Operation(summary = "새로운 비밀번호 발급", description = "새로운 비밀번호를 발급합니다. 새로운 비밀번호는 해당 아이디의 이메일로 발송됩니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 재발급 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<String> newPassword(@Size(min = 5, max = 30) @Schema(example = "user12")
                                              @RequestParam("username")
                                              String username,

                                              @Size(min = 1, max = 50) @Schema(example = "example@naver.com")
                                              @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                      message = "유효한 이메일이 아닙니다.")
                                              @RequestParam("email") String email) {

        String newPassword = memberService.createNewPassword(username, email);
        findMemberEmailService.sendNewPasswordEmail(email, newPassword);

        return ResponseEntity.ok("비밀번호 재발급에 성공하였습니다");
    }


    @MethodTimer(method = "아이디 찾기 호출")
    @GetMapping("/username")
    @Operation(summary = "아이디 찾기", description = "해당 이메일로 회원가입이 된 아이디가 존재한다면, 해당 이메일로 아이디를 발송합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "아이디 전송 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<String> findUsername(@Size(min = 1, max = 50) @Schema(example = "example@naver.com")
                                               @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                        message = "유효한 이메일이 아닙니다.")
                                               @RequestParam("email") String email) {

        String username = memberService.findUsernameByEmail(email);
        findMemberEmailService.sendUsernameEmail(email, username);

        return ResponseEntity.ok("아이디 찾기를 성공하였습니다");
    }
}

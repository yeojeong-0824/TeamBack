package com.example.demo.member.member.presentation;

import com.example.demo.member.member.application.MemberService;
import com.example.demo.member.member.application.MemberServiceImpl;
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
import org.springframework.http.HttpStatus;
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

    private final MemberService memberServiceImpl;
    private final FindMemberEmailService findMemberEmailService;

    @PatchMapping("/password")
    @Operation(summary = "새로운 비밀번호 발급")
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
                                              @RequestParam("email") String email,

                                              HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 새로운 비밀번호 발급 호출", ip);

        String newPassword = memberServiceImpl.createNewPassword(username, email);
        findMemberEmailService.sendNewPasswordEmail(email, newPassword);

        return ResponseEntity.ok("비밀번호 재발급에 성공하였습니다");
    }

    @GetMapping("/username")
    @Operation(summary = "아이디 찾기")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "아이디 전송 완료"),
                    @ApiResponse(responseCode = "400", description = "입력 값이 잘못됨"),
            }
    )
    public ResponseEntity<String> findUsername(@Size(min = 1, max = 50) @Schema(example = "example@naver.com")
                                               @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                        message = "유효한 이메일이 아닙니다.")
                                               @RequestParam("email") String email,

                                               HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 아이디 찾기 호출", ip);

        String username = memberServiceImpl.findUsernameByEmail(email);
        findMemberEmailService.sendUsernameEmail(email, username);

        return ResponseEntity.ok("아이디 찾기를 성공하였습니다");
    }
}

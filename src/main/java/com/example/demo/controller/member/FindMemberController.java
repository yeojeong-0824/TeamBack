package com.example.demo.controller.member;

import com.example.demo.service.email.EmailService;
import com.example.demo.service.email.FindMemberEmailService;
import com.example.demo.service.member.MemberService;
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

    private final MemberService memberService;
    private final FindMemberEmailService findMemberEmailService;

    // Todo: http 상태 코드 수정해야 함
    @PatchMapping("/{username}")
    @Operation(summary = "새로운 비밀번호 발급")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 재발급 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
            }
    )
    public ResponseEntity<String> newPassword(@Size(min = 5, max = 30) @Schema(example = "user12")
                                              @PathVariable("username")
                                              String username,

                                              @Size(min = 1, max = 50) @Schema(example = "example@naver.com")
                                              @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                      message = "유효한 이메일이 아닙니다.")
                                              @RequestBody String email,

                                              HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 새로운 비밀번호 발급 호출", ip);

        String savedMemberEmail = memberService.findMemberEmailByUsername(username);
        if(!email.equals(savedMemberEmail)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 잘못되었습니다.");

        String password = findMemberEmailService.createNewPassword();

        findMemberEmailService.sendNewPasswordEmail(savedMemberEmail, password);
        memberService.patchPasswordByUsername(username, password);

        return ResponseEntity.ok("비밀번호 재발급에 성공하였습니다");
    }
}

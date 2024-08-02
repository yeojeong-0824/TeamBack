package com.example.demo.controller.member;

import com.example.demo.dto.member.MemberDetails;
import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.email.EmailService;
import com.example.demo.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/authed")
@Tag(name = "유저 API (Authed)")
@PreAuthorize("isAuthenticated()")
public class AuthedMemberController {

    private final MemberService memberService;

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 변경 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
            }
    )
    public ResponseEntity<String> patchPassword(@NotBlank @Size(min = 8, max = 30)
                                                @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])\\S+$", // 비밀번호 정규식
                                                         message = "비밀번호는 영문(대,소문자)과 숫자가 적어도 1개 이상씩 포함되어야 합니다")
                                                @Schema(example = "1q2w3e4r")
                                                @RequestBody String password,
                                                HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 비밀번호 변경 호출", ip);

        MemberDetails memberDetails = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = memberDetails.getUsername();

        memberService.patchPasswordByUsername(username, password);
        return ResponseEntity.ok("비밀번호 변경에 성공하였습니다");
    }

    @PatchMapping("/nickname")
    @Operation(summary = "닉네임 변경")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 변경 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
            }
    )
    public ResponseEntity<String> patchNickname(@NotBlank @Size(min = 1, max = 10)
                                                @Schema(example = "소인국갔다옴")
                                                @RequestBody String nickname,
                                                HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 닉네임 변경 호출", ip);

        MemberDetails memberDetails = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = memberDetails.getUsername();

        memberService.patchNicknameByUsername(username, nickname);
        return ResponseEntity.ok("닉네임 변경에 성공하였습니다");
    }
}

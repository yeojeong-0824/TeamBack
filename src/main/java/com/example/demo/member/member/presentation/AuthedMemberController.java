package com.example.demo.member.member.presentation;

import com.example.demo.config.util.SecurityUtil;
import com.example.demo.member.member.application.MemberService;
import com.example.demo.member.member.presentation.dto.MemberDetails;
import com.example.demo.member.member.application.MemberServiceImpl;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/authed")
@Tag(name = "유저 API (Authed)")
@PreAuthorize("isAuthenticated()")
public class AuthedMemberController {

    private final MemberService memberServiceImpl;

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 변경 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
            }
    )
    public ResponseEntity<String> patchPassword(@Valid @RequestBody MemberRequest.patchPassword takenDto,
                                                HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 비밀번호 변경 호출", ip);

        Long userId = SecurityUtil.getCurrentUserId();

        memberServiceImpl.patchPasswordByUsername(userId, takenDto.password());
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
    public ResponseEntity<String> patchNickname(@Valid @RequestBody MemberRequest.patchNickname takenDto,
                                                HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 닉네임 변경 호출", ip);

        Long userId = SecurityUtil.getCurrentUserId();

        memberServiceImpl.patchNicknameById(userId, takenDto.nickname());
        return ResponseEntity.ok("닉네임 변경에 성공하였습니다");
    }
}

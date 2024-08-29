package com.example.demo.member.member.presentation;

import com.example.demo.security.SecurityUtil;
import com.example.demo.member.member.application.MemberService;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.member.presentation.dto.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/authed")
@Tag(name = "유저 API (Authed)")
@PreAuthorize("isAuthenticated()")
public class AuthedMemberController {

    private final MemberService memberService;
    @GetMapping
    @Operation(summary = "회원 정보 확인", description = "간단한 회원 정보를 받아옵니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<MemberResponse.FindMember> findById(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 회원정보 엔드포인트 호출", ip);

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.findById(memberId));
    }

    @GetMapping("/board")
    @Operation(summary = "해당 회원의 작성 게시글 확인", description = "해당 회원이 작성한 게시글의 정보를 받아옵니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Page<MemberResponse.BoardInfo>> findBoardById(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                                                                        HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 작성한 게시글 엔드포인트 호출", ip);

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.findBoardById(memberId, page));
    }

    @GetMapping("/comment")
    @Operation(summary = "해당 회원의 별점 등록 목록 확인", description = "해당 회원이 등록한 별점의 정보를 받아옵니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Page<MemberResponse.CommentInfo>> findBoardScoreById(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                                                                             HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 작성한 댓글 엔드포인트 호출", ip);

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.findCommentById(memberId, page));
    }

    @DeleteMapping
    @Operation(summary = "유저 탈퇴", description = "회원 탈퇴를 진행합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "유저 탈퇴"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<String> deleteByUserId(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                 @Valid @RequestBody MemberRequest.DeleteMember takenDto,
                                                 HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 유저 탈퇴 엔드포인트 호출", ip);

        Long memberId = SecurityUtil.getCurrentMemberId();
        memberService.deleteByMemberId(memberId, takenDto);

        return ResponseEntity.ok("유저 탈퇴에 성공했습니다");
    }

    @PatchMapping
    @Operation(summary = "유저 정보 수정", description = "회원 정보를 수정합니다")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "수정 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<String> patchPassword(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                @Valid @RequestBody MemberRequest.PatchMember takenDto,
                                                HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: 회원 정보 수정 엔드포인트 호출", ip);

        Long memberId = SecurityUtil.getCurrentMemberId();
        memberService.patchById(memberId, takenDto);

        return ResponseEntity.ok("회원 정보 수정 성공하였습니다");
    }
}

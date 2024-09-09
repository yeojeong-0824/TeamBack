package com.example.demo.domain.member.member.presentation;

import com.example.demo.config.util.customannotation.MethodTimer;
import com.example.demo.domain.member.member.presentation.dto.MemberResponse;
import com.example.demo.security.SecurityUtil;
import com.example.demo.domain.member.member.application.memberservice.MemberService;
import com.example.demo.domain.member.member.presentation.dto.MemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/vi/members/authed")
@Tag(name = "유저 API (Authed)")
@PreAuthorize("isAuthenticated()")
public class AuthedMemberController {

    private final MemberService memberService;

    @MethodTimer(method = "회원 정보 호출")
    @GetMapping
    @Operation(summary = "회원 정보 확인", description = "간단한 회원 정보를 받아옵니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<MemberResponse.FindMember> findById() {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.findById(memberId));
    }

    @MethodTimer(method = "해당 회원이 작성한 게시글 호출")
    @GetMapping("/boards")
    @Operation(summary = "해당 회원의 작성 게시글 확인", description = "해당 회원이 작성한 게시글의 정보를 받아옵니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Page<MemberResponse.BoardInfo>> findBoardById(@RequestParam(required = false, defaultValue = "1", value = "page") int page) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.findBoardById(memberId, page));
    }

    @MethodTimer(method = "해당 회원이 작성한 댓글 호출")
    @GetMapping("/comments")
    @Operation(summary = "해당 회원의 작성 댓글 목록 확인", description = "해당 회원이 작성한 댓글의 정보를 받아옵니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Page<MemberResponse.CommentInfo>> findBoardScoreById(@RequestParam(required = false, defaultValue = "1", value = "page") int page) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.findCommentById(memberId, page));
    }

    @MethodTimer(method = "회원 탈퇴 호출")
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
                                                 @Valid @RequestBody MemberRequest.DeleteMember takenDto) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        memberService.deleteByMemberId(memberId, takenDto);

        return ResponseEntity.ok("유저 탈퇴에 성공했습니다");
    }

    @MethodTimer(method = "회원 정보 수정 호출")
    @PatchMapping
    @Operation(summary = "유저 정보 수정", description = "회원 정보를 수정합니다")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "수정 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<MemberResponse.FindMember> patchMember(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                                 @Valid @RequestBody MemberRequest.PatchMember takenDto) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.patchById(memberId, takenDto));
    }

    @MethodTimer(method = "회원 비밀번호 수정 호출")
    @PatchMapping("/passwords")
    @Operation(summary = "유저 비밀번호 수정", description = "회원 비밀번호 수정 호출")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "수정 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<MemberResponse.FindMember> patchPassword(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                                   @Valid @RequestBody MemberRequest.PatchPassword takenDto) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.patchPasswordById(memberId, takenDto));
    }
}

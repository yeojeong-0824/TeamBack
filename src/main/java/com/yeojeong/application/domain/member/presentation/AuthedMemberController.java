package com.yeojeong.application.domain.member.presentation;

import com.yeojeong.application.config.util.customannotation.MethodTimer;
import com.yeojeong.application.domain.member.application.memberfacade.MemberFacade;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import com.yeojeong.application.security.config.SecurityUtil;
import com.yeojeong.application.domain.member.presentation.dto.MemberRequest;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members/authed")
@Tag(name = "유저 API (Authed)")
public class AuthedMemberController {

    private final MemberFacade memberFacade;

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
    public ResponseEntity<MemberResponse.FindById> findById() {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberFacade.findById(id));
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

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberFacade.findBoardById(id, page));
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

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberFacade.findCommentById(id, page));
    }

    @MethodTimer(method = "회원 탈퇴 호출")
    @DeleteMapping
    @Operation(summary = "유저 탈퇴", description = "회원 탈퇴를 진행합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "유저 탈퇴"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Void> deleteByUserId(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                 @Valid @RequestBody MemberRequest.checkPassword dto) {

        Long id = SecurityUtil.getCurrentMemberId();
        memberFacade.delete(id, dto);

        return ResponseEntity.noContent().build();
    }

    @MethodTimer(method = "비밀번호 확인")
    @PostMapping("/checkPassword")
    @Operation(summary = "비밀번호 확인", description = "회원 비밀번호를 확인합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "비밀번호 확인 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "비밀번호가 일치하지 않음"),
            }
    )
    public ResponseEntity<MemberResponse.patchKey> checkPassword(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                              @Valid @RequestBody MemberRequest.checkPassword dto) {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberFacade.checkPassword(id, dto.password()));
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
    public ResponseEntity<MemberResponse.FindById> patchMember(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                               @Valid @RequestBody MemberRequest.Patch dto) {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberFacade.patch(id, dto));
    }

    @MethodTimer(method = "회원 비밀번호 수정 호출")
    @PatchMapping("/password")
    @Operation(summary = "유저 비밀번호 정보 수정", description = "회원 비밀번호 정보를 수정합니다")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "수정 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<MemberResponse.FindById> patchPasswordMember(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                                       @Valid @RequestBody MemberRequest.PatchPassword dto) {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberFacade.patchPassword(id, dto));
    }
}

package com.yeojeong.application.domain.member.presentation;

import com.yeojeong.application.domain.member.application.memberfacade.MemberFacade;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import com.yeojeong.application.security.config.SecurityUtil;
import com.yeojeong.application.domain.member.presentation.dto.MemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members/authed")
@Tag(name = "유저 API (Authed)")
public class AuthedMemberController {

    private final MemberFacade memberFacade;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원 정보 확인", security = @SecurityRequirement(name = "bearerAuth"))
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

    @GetMapping(value = "/boards", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "해당 회원의 작성 게시글 확인", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Page<MemberResponse.MemberBoardInfo>> findBoardById(
            @RequestParam(required = false, defaultValue = "1", value = "page") int page
    ) {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberFacade.findBoardById(id, page));
    }

    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "해당 회원의 작성 댓글 목록 확인", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Page<MemberResponse.MemberCommentInfo>> findBoardScoreById(
            @RequestParam(required = false, defaultValue = "1", value = "page") int page
    ) {

        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberFacade.findCommentById(id, page));
    }

    @GetMapping(value = "/planners", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "해당 회원의 플레너 확인", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Page<MemberResponse.MemberPlannerInfo>> findPlannerById(
            @RequestParam(required = false, defaultValue = "1", value = "page") int page
    ) {
        Long id = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberFacade.findPlannerById(id, page));
    }

    @GetMapping(value = "/locations", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "날짜 범위에 대한 장소를 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "날짜 범위에 대한 장소 조회 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    public ResponseEntity<List<MemberResponse.MemberLocationInfo>> findLocationByDate(
            @RequestParam("start") Long start, @RequestParam("end"
    ) Long end) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(memberFacade.findLocationByDate(memberId, start, end));
    }

    @DeleteMapping
    @Operation(summary = "유저 탈퇴", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "유저 탈퇴"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Void> deleteByUserId() {

        Long id = SecurityUtil.getCurrentMemberId();
        memberFacade.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/checkPassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "비밀번호 확인", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "비밀번호 확인 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "비밀번호가 일치하지 않음"),
            }
    )
    public ResponseEntity<Void> checkPassword(@Valid @RequestBody MemberRequest.CheckPassword dto) {

        Long id = SecurityUtil.getCurrentMemberId();
        memberFacade.checkPassword(id, dto.password());
        return ResponseEntity.noContent().build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 정보 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "수정 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Void> update(@Valid @RequestBody MemberRequest.Put dto) {

        Long id = SecurityUtil.getCurrentMemberId();
        memberFacade.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 비밀번호 정보 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "수정 완료"),
                    @ApiResponse(responseCode = "400", description = "유저를 찾지 못함"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
            }
    )
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody MemberRequest.PatchPassword dto) {

        Long id = SecurityUtil.getCurrentMemberId();
        memberFacade.updatePassword(id, dto);
        return ResponseEntity.ok().build();
    }
}
